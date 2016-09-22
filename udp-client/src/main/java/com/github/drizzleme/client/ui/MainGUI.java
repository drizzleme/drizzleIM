package com.github.drizzleme.client.ui;

import com.eva.epc.widget.HardLayoutPane;
import com.github.drizzleme.client.ClientCoreSDK;
import com.github.drizzleme.client.core.LocalUDPDataSender;
import com.github.drizzleme.core.util.Log;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class MainGUI extends JFrame{
        private JButton btnLogout = null;

        private JTextField editId = null;
        private JTextField editContent = null;
        private JButton btnSend = null;
        private JLabel viewMyid = null;

        private JTextPane debugPane;
        private JTextPane imInfoPane;

        private SimpleDateFormat hhmmDataFormat = new SimpleDateFormat("HH:mm:ss");

        public MainGUI()
        {
            initViews();
            initListeners();
            initOthers();
        }

        private void initViews()
        {
            // 登陆组件初始化
            btnLogout = new JButton("退出程序");
            btnLogout.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
            btnLogout.setForeground(Color.white);
            viewMyid = new JLabel();
            viewMyid.setForeground(new Color(255,0,255));
            viewMyid.setText("未登陆");

            // 消息发送组件初始化
            btnSend = new JButton("发送消息");
            btnSend.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
            btnSend.setForeground(Color.white);
            editId = new JTextField(20);
            editContent = new JTextField(20);

            // debug信息显示面板初始化
            debugPane=new JTextPane();
            debugPane.setBackground(Color.black);
            debugPane.setCaretColor(Color.white);
            //
            Log.getInstance().setLogDest(debugPane);

            // IM通讯信息显示面板初始化
            imInfoPane=new JTextPane();

            // 登陆认证信息布局
            JPanel authPane = new JPanel();
            authPane.setLayout(new BoxLayout(authPane, BoxLayout.LINE_AXIS));
            authPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
            authPane.add(new JLabel("我的id："));
            authPane.add(viewMyid);
            authPane.add(Box.createHorizontalGlue());
            authPane.add(btnLogout);

            // 消息发送布局
            HardLayoutPane toPanel = new HardLayoutPane();
            toPanel.addTo(new JLabel("对方ID号："), 1, true);
            toPanel.addTo(editId, 1, true);
            toPanel.nextLine();
            toPanel.addTo(new JLabel("发送内容："), 1, true);
            toPanel.addTo(editContent, 1, true);
            toPanel.nextLine();
            toPanel.addTo(btnSend, 4, true);
            toPanel.nextLine();

            HardLayoutPane oprMainPanel = new HardLayoutPane();
            oprMainPanel.addTitledLineSeparator("登陆认证");
            oprMainPanel.addTo(authPane, 1, true);
            oprMainPanel.addTitledLineSeparator("消息发送");
            oprMainPanel.addTo(toPanel, 1, true);
            oprMainPanel.addTitledLineSeparator();

            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(oprMainPanel, BorderLayout.NORTH);
            JScrollPane imInfoSc = new JScrollPane(imInfoPane);
            imInfoSc.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 7, 0, 7), imInfoSc.getBorder()));
            imInfoSc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            leftPanel.add(imInfoSc, BorderLayout.CENTER);

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(leftPanel, BorderLayout.WEST);
            JScrollPane sc = new JScrollPane(debugPane);
            sc.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(4, 0, 0, 2), sc.getBorder()));
            sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.getContentPane().add(sc, BorderLayout.CENTER);

            this.setTitle("MobileIMSDK演示工程");
            this.setLocationRelativeTo(null);
            this.setSize(1000,700);
        }

        private void initListeners()
        {
            btnLogout.addActionListener(e -> {
                // 退出登陆
                doLogout();
                // 退出程序
                doExit();
            });

            btnSend.addActionListener(e -> doSendMessage());

            this.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    // 退出登陆
                    doLogout();
                    // 退出程序
                    doExit();
                }
            });
        }

        private void initOthers()
        {
            // Refresh userId to show
            refreshMyid();

            // Set MainGUI instance refrence to listeners
            IMClientManager.getInstance().getTransDataListener().setMainGUI(this);
            IMClientManager.getInstance().getBaseEventListener().setMainGUI(this);
            IMClientManager.getInstance().getMessageQoSListener().setMainGUI(this);
        }

        public void refreshMyid()
        {
            int myid = ClientCoreSDK.getInstance().getCurrentUserId();
            this.viewMyid.setText(myid == -1 ? "连接断开" :""+myid);
        }

        private void doSendMessage()
        {
            final String msg = editContent.getText().toString().trim();
            if(msg.length() > 0)
            {
                int friendId = -1;
                String friendIdStr = editId.getText().toString().trim();
                try{
                    friendId = Integer.parseInt(friendIdStr);
                }
                catch (Exception e){
                    MainGUI.this.showIMInfo_red("无效的接收方ID号："+friendIdStr);
                }

                if(friendId > 0)
                {
                    MainGUI.this.showIMInfo_black("我对"+friendId+"说："+msg);

                    // 发送消息（异步提升体验，你也可直接调用LocalUDPDataSender.send(..)方法发送）
                    new LocalUDPDataSender.SendCommonDataAsync(msg, friendId, true)
                    {
                        @Override
                        protected void onPostExecute(Integer code)
                        {
                            if(code == 0)
                                Log.i(MainGUI.class.getSimpleName(), "2数据已成功发出！");
                            else
                                showToast("数据发送失败。错误码是："+code+"！");
                        }
                    }.execute();
                }
            }
            else
                Log.e(MainGUI.class.getSimpleName(), "消息内容长度="+(msg.length()));
        }

        private void doLogout()
        {
            // 发出退出登陆请求包
            int code = LocalUDPDataSender.getInstance().sendLoginout();
            refreshMyid();
            if(code == 0)
                System.out.println("注销登陆请求已完成！");
            else
                System.out.println("注销登陆请求发送失败。错误码是："+code+"！");
        }

        public static void doExit()
        {
            // 释放IM占用资源
//		ClientCoreSDK.getInstance().release();
            IMClientManager.getInstance().release();
            // JVM退出
            System.exit(0);
        }

        //--------------------------------------------------------------- 控制台输出和Toast显示方法 START
        public void showIMInfo_black(String txt)
        {
            showIMInfo(new Color(0,0,0), txt);
        }
        public void showIMInfo_blue(String txt)
        {
            showIMInfo(new Color(0,0,255), txt);
        }
        public void showIMInfo_brightred(String txt)
        {
            showIMInfo(new Color(255,0,255), txt);
        }
        public void showIMInfo_red(String txt)
        {
            showIMInfo(new Color(255,0,0), txt);
        }
        public void showIMInfo_green(String txt)
        {
            showIMInfo(new Color(0,128,0), txt);
        }
        public void showIMInfo(Color c, String txt)
        {
            try{
                Log.append(c, "["+hhmmDataFormat.format(new Date())+"]"+txt+"\r\n", this.imInfoPane);
                imInfoPane.setCaretPosition(imInfoPane.getDocument().getLength());
            }
            catch(Exception e){
//			e.printStackTrace();
            }
        }

        public void showToast(String text)
        {
            Toast.showTost(3000, text, new Point((int)(this.getLocationOnScreen().getX())+50,
                    (int)(this.getLocationOnScreen().getY())+400));
        }
        //--------------------------------------------------------------- 控制台输出和Toast显示方法 END
}
