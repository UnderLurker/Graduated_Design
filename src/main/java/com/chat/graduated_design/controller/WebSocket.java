package com.chat.graduated_design.controller;

import com.chat.graduated_design.config.WebSocketConfig;
import com.chat.graduated_design.entity.chat.chatInfo;
import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.service.impl.chatInfoServiceImpl;
import com.chat.graduated_design.service.impl.contactServiceImpl;
import com.chat.graduated_design.util.DateUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.chat.graduated_design.controller.code.ServerEncoder;
import org.springframework.boot.json.JsonParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @program: Graduated_Design
 * @description: 实现自己的websocket
 * @author: 常笑男
 * @create: 2022-02-23 10:58
 **/
@ServerEndpoint(value = "/chat",configurator = WebSocketConfig.class,encoders = ServerEncoder.class)
@Component
public class WebSocket {
    //用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();
    //用来记录id和该session进行绑定
    private static Map<Integer, Session> map = new HashMap<Integer, Session>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //用户id
    private Integer userid;
    //获取全局容器
    private ApplicationContext applicationContext;

    private chatInfoServiceImpl chatInfoService;

    private contactServiceImpl contactService;

    public Session getSessionById(Integer id){
        return map.get(id);
    }

    /**
     * 连接建立成功调用的方法，初始化id、session
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        //获取登录时存放httpSession的用户数据
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(httpSession.getServletContext());

        User user = (User) httpSession.getAttribute("user");

        this.applicationContext = applicationContext;
        this.session = session;
        this.userid=user.getId();
        this.chatInfoService = (chatInfoServiceImpl) applicationContext.getBean("chatInfoServiceImpl");
        this.contactService=(contactServiceImpl) applicationContext.getBean("contactServiceImpl");

        //绑定username与session
        map.put(userid, session);

        webSocketSet.add(this);     //加入set中

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //断开连接，重置窗口值
//        chatService.resetWindows(userid);
        webSocketSet.remove(this);  //从set中删除

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {

        //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为chatMsg对象，
        ObjectMapper objectMapper = new ObjectMapper();
        chatInfo chatMsg;

        try {
            chatMsg = objectMapper.readValue(message, chatInfo.class);

            Date saveDate= DateUtil.getCurrentTime();
            //对chatMsg进行装箱
            chatMsg.setTime(saveDate);

            Session fromSession = map.get(chatMsg.getOrigin());
            Session toSession = map.get(chatMsg.getDest());

            Map<String,Object> sendInfo=new HashMap<>();
            sendInfo.put("content",chatMsg.getContent());
            sendInfo.put("dest",chatMsg.getDest());
            sendInfo.put("origin",chatMsg.getOrigin());
            sendInfo.put("readFlag",true);
            sendInfo.put("time",saveDate);
            sendInfo.put("file",false);

            //发送给origin
            fromSession.getAsyncRemote().sendObject(sendInfo);

            if (toSession != null&&toSession.isOpen()) {
                //发送给dest
                toSession.getAsyncRemote().sendObject(sendInfo);
            }
            else{
                //当发送的目的用户未登录，将未读+1
                Map<String,Object> query=new HashMap<>();
                query.put("userid",chatMsg.getOrigin());
                query.put("contactid",chatMsg.getDest());
                List<contact> queryResult=contactService.listByMap(query);
                for (contact person: queryResult) {
                    person.setUnread(person.getUnread()+1);
                }
                contactService.updateBatchById(queryResult);
            }
            //保存聊天记录信息
            chatInfoService.save(chatMsg);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public void broadcast(String message) {
        for (WebSocket item : webSocketSet) {

            //异步发送消息.
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public Date CSTToGMT(Date CSTTime){
        DateFormat cstFormat=new SimpleDateFormat();
        TimeZone gmtZone=TimeZone.getTimeZone("GMT");
        cstFormat.setTimeZone(gmtZone);
        Date result=null;
        try {
            result=cstFormat.parse(cstFormat.format(CSTTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
