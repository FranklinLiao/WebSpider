package org.net.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class AddressMatch extends JFrame {
	JButton button = null;
	JProgressBar progressBar = null;
	JLabel label;

    public AddressMatch() {
        super();
        setTitle("中国IPv6地址块匹配");
        setBounds(100,100,400,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        label = new JLabel("点击按钮开始匹配", JLabel.CENTER);
        label.setForeground(Color.red);            //设置前景色
        progressBar = new JProgressBar();
        progressBar.setOrientation(JProgressBar.HORIZONTAL);
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setBorderPainted(true);
        progressBar.setBackground(Color.green);
        progressBar.setStringPainted(true);        //显示提示信息
        progressBar.setIndeterminate(true);        //不确定进度的进度条
        progressBar.setString("正在匹配中..."); 
        progressBar.setVisible(false);
        
        JPanel panel = new JPanel();
        button = new JButton("开始匹配");
        button.setForeground(Color.blue);
        button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				JButton btn = (JButton)obj;
				if (btn.getText()=="开始匹配") {
					new Progress(progressBar,button,label).start();   //自定义类progress
					label.setText("匹配进行中.....");
			    	button.setEnabled(false);
			    	progressBar.setVisible(true);
			    }
				if (btn.getText()=="完成") {
			    	System.exit(0);
			    }				
			}
		});     
        panel.add(button);
        
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(label, BorderLayout.CENTER);
        getContentPane().add(progressBar, BorderLayout.SOUTH);      
    }
    

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	AddressMatch addressMatch = new AddressMatch();
    	addressMatch.setVisible(true);
    }

}

    
class Progress extends Thread{//自定义类progress
    private JProgressBar progressBar;
    private JButton button;
    private JLabel  label;
    public Progress(JProgressBar progressBar,JButton button,JLabel label)
    {
        this.progressBar = progressBar;
        this.button =button;
        this.label = label;
    }
    public void run()
    {
        BlockMatch.match();
        progressBar.setMaximum(100);
        progressBar.setValue(100);
        progressBar.setString("匹配完成.");  //提示信息
        progressBar.setIndeterminate(false);  
        button.setText("完成");
        button.setEnabled(true);  //按钮可用    
        label.setText("点击完成按钮退出程序");
    }
}