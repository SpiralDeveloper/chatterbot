package chatterbot.app.bits.fabric.chatterbot;

import java.util.ArrayList;
import java.util.List;

public class TestItem {
    public String body;
    public boolean left;
    public String time;
    public int type;

    public TestItem(String body,boolean left,String time,int type){
           this.body=body;
           this.left=left;
           this.time=time;
           this.type=type;
    }

    public static List<TestItem> getData(){
        List<TestItem> list=new ArrayList<>();
        list.add(new TestItem("Hello John How can I help you?",true,"11:05 pm",0));
        list.add(new TestItem("Hey jack please remind me to cal Jack Ryan, tomorrow afternoon.",false,"11:05 pm",1));
        list.add(new TestItem("Cool I'll set new reminder for you. Will remind you tomorrow between 12 am - 04 pm.",true,"04:05 pm",0));
        list.add(new TestItem("Barclays misled shareholders and the public about one of the biggest investments in the bank's history, a BBC Pano",true,"11:05 pm",1));
        list.add(new TestItem("Half of the cash was supposed to be coming",false,"11:05 pm",0));
        list.add(new TestItem("Hey jack please remind me to cal Jack Ryan, tomorrow afternoon.",false,"11:05 pm",0));
        list.add(new TestItem("Hey jack please remind me to cal Jack Ryan, tomorrow afternoon.",false,"11:05 pm",2));
        list.add(new TestItem("Hey jack please remind me to cal Jack Ryan, tomorrow afternoon.",false,"11:05 pm",0));
        list.add(new TestItem("Cool I'll set new reminder for you. Will remind you tomorrow between 12 am - 04 pm.",true,"04:05 pm",1));
        list.add(new TestItem("Barclays misled shareholders and the public about one of the biggest investments in the bank's history, a BBC Pano",true,"11:05 pm",1));
        list.add(new TestItem("Half of the cash was supposed to be coming",false,"11:05 pm",0));
        list.add(new TestItem("Cool I'll set new reminder for you. Will remind you tomorrow between 12 am - 04 pm.",true,"04:05 pm",1));
        return list;
    }
}
