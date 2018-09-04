package org.remover;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.HTTPUtils;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureTypeList;


public class removeTool  extends JFrame {
	private  String RESTURL  = "";
	private  String RESTUSER = "";
	private  String RESTPW   = "";
	private  String WORKSPACE = "";
	private  String DATASTORE = "";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					removeTool frame = new removeTool();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public removeTool() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JLabel lengthLabel = new JLabel("地址：");
		final JTextField aText = new JTextField(20);
		lengthLabel.setLocation(10, 10);
		lengthLabel.setSize(60,30);
		aText.setBounds(60, 10, 300, 30);
		aText.setText("http://192.168.0.195/geoserver");
		JLabel nameLabel = new JLabel("用户名：");
		JLabel passLabel = new JLabel("密码：");
		final JTextField nameText = new JTextField(20);
		final JPasswordField passwordField = new JPasswordField(20);
		nameLabel.setBounds(10, 40, 60, 30);
		nameText.setBounds(60, 40, 100, 30);
		nameText.setText("admin");
		//密码
		passLabel.setBounds(10, 70, 60, 30);
		passwordField.setBounds(60, 70, 100, 30);
		passwordField.setText("geoserver");
		//工作、存储空间
		JLabel wsLabel = new JLabel("workspace：");
		final JTextField wsText = new JTextField(20);
		wsLabel.setBounds(10, 100, 100, 30);
		wsText.setBounds(100, 100, 100, 30);
		wsText.setText("neto");
		JLabel dsLabel = new JLabel("datastore：");
		final JTextField dsText = new JTextField(20);
		dsLabel.setBounds(10, 130, 100, 30);
		dsText.setBounds(100, 130, 100, 30);
		dsText.setText("postgis");
		//图层名
		JLabel layerLabel = new JLabel("图层名：");
		final JTextField layerText = new JTextField(20);
		layerLabel.setBounds(10, 160, 70, 30);
		layerText.setBounds(70, 160, 300, 30);
		//删除
		JButton button = new JButton("删除图层");
		button.setBounds(170, 200, 100, 30);
		Container container = getContentPane();
		getContentPane().setLayout(null);
		container.add(lengthLabel);
		container.add(nameLabel);
		//用户名
		container.add(aText);
		container.add(nameText);
		//密码
		container.add(passLabel);
		container.add(passwordField);
		//工作、存储空间
		container.add(wsLabel);
		container.add(wsText);
		container.add(dsLabel);
		container.add(dsText);
		//图层名
		container.add(layerLabel);
		container.add(layerText);
		//删除
		container.add(button);
		button.addActionListener(new ActionListener(){
		       public void actionPerformed(ActionEvent e) {
		    	   try {
		    		   RESTURL= aText.getText();
		    		   RESTUSER=nameText.getText();
		    		   RESTPW=passwordField.getText();
		    		   WORKSPACE=wsText.getText();
		    		   DATASTORE=dsText.getText();
		    		   String layerName=layerText.getText();
		    		   System.out.println(RESTURL+"用户名："+RESTUSER+"密码"+RESTPW+"工作空间："+WORKSPACE+"存储"+DATASTORE);
		    		   System.out.println("图层名："+layerName);
		    		   if(RESTURL.isEmpty()) {
		    			   JOptionPane.showMessageDialog(null,"地址为空,请填写正确！", "地址", JOptionPane.WARNING_MESSAGE);
		    			   return;
		    		   }
		    		   if(RESTUSER.isEmpty()) {
		    			   JOptionPane.showMessageDialog(null,"用户名为空,请填写正确！", "用户名", JOptionPane.WARNING_MESSAGE);
		    			   return;
		    		   }
		    		   if(RESTPW.isEmpty()) {
		    			   JOptionPane.showMessageDialog(null,"密码为空,请填写正确！", "密码", JOptionPane.WARNING_MESSAGE);
		    			   return;
		    		   }
		    		   if(WORKSPACE.isEmpty()) {
		    			   JOptionPane.showMessageDialog(null,"工作空间为空,请填写正确！", "工作空间", JOptionPane.WARNING_MESSAGE);
		    			   return;
		    		   }
		    		   if(DATASTORE.isEmpty()) {
		    			   JOptionPane.showMessageDialog(null,"存储空间为空,请填写正确！", "存储空间", JOptionPane.WARNING_MESSAGE);
		    			   return;
		    		   }
		    		   if(layerName.isEmpty()) {
		    			   JOptionPane.showMessageDialog(null,"图层名为空,请填写正确！", "图层名", JOptionPane.WARNING_MESSAGE);
		    			   return;
		    		   }
		    		   removeLayer(RESTURL,layerName);  
		    		  
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}});
	}
	public void removeLayer(String url ,String layerName) throws IOException{
		 boolean isExist  = this.isLayerExist(url, layerName);
		 if(isExist){
			 try {
//				 	gwcKillTask(url,layerName);
//				 	TimeUnit.SECONDS.sleep(10);//等待10s
					GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(url, RESTUSER, RESTPW);
					 boolean removed = publisher.removeLayer(WORKSPACE, layerName);
					 
					 if(removed){
						 TimeUnit.SECONDS.sleep(10);//等待10s
						 publisher.reload();
						 JOptionPane.showMessageDialog(null,"图层删除成功！", "图层删除", JOptionPane.INFORMATION_MESSAGE);
					 }else{
						 JOptionPane.showMessageDialog(null,"图层删除失败！", "图层删除", JOptionPane.ERROR);
					 }
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		 }else {
			 JOptionPane.showMessageDialog(null,"该图层不存在！", "图层名", JOptionPane.ERROR_MESSAGE);
		 }
		
				 
	}
	public Boolean isLayerExist(String url ,String layername) throws IOException{
		GeoServerRESTReader reader = new GeoServerRESTReader(url,this.RESTUSER, this.RESTPW);
		RESTFeatureTypeList layers=this.getFeatureTypes(url, this.RESTUSER, this.RESTPW);
		List<String> alllayers = layers.getNames();
		Boolean isexist =false;
		for(String layer:alllayers){
			if(layer.equals(layername)){
				isexist= true;
			}
		}
		return isexist;
	}
	public RESTFeatureTypeList getFeatureTypes(String url,String username, String password)
	  {
	   String respone = load(url,username,password);
	    return RESTFeatureTypeList.build(load(url,username,password));
	  }
	private String load(String baseurl,String username, String password) {
		    try {
		      return HTTPUtils.get(baseurl+"/rest/workspaces/"+this.WORKSPACE+"/datastores/"+this.DATASTORE+"/featuretypes.xml", username, password);
		    }catch(Exception e) {
		    	
		    }

		    return null;
		  }

}
