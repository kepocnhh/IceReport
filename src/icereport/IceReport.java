package icereport;

import api.API;
import api.CreateXLS;
import ice.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IceReport
{
    static private String newpath = "/home/toha/ForIce/NewLogs/";
    static private String new_accpath = "/home/toha/ForIce/account_new";
    static private String xlspath = "/home/toha/ForIce/newXLS";
    public static void main(String[] args)
    {
            Date bgn = new Date();
            bgn.setYear(114);
            bgn.setMonth(6);
            bgn.setDate(1);
            Date end = new Date();
            end.setYear(114);
            end.setMonth(6);
            end.setDate(30);
                String path = (bgn.getYear()+1900)+"/"+(bgn.getMonth()+1)+"/"+bgn.getDate()+"/";
        Itog myitog;//объект итогов пользователя
        List<String> stringlist = null;
        List<BaseMessage> loglist = null;
        List<user> userlist = new ArrayList();
        try
        {
            stringlist = API.Get_String_List(new_accpath); //список с данными пользователей
            loglist = API.Get_BM_List(newpath+ path+"Добровольская 1.7.2014.ice"); //список с данными пользователей
        }
        catch (IOException ex){}
        catch (ClassNotFoundException ex) {}
            for (String bmlist1 : stringlist)
            {
                userlist.add(new user(bmlist1));
            }
                    user newuser = null;
        for (user userlist1 : userlist) 
        {
            if ((newpath+ path+"Добровольская 1.7.2014.ice").lastIndexOf(userlist1.GetSurname()) != -1) 
            {
                newuser = userlist1;
                break;
            }
        }
        myitog = API.Get_Itog(newuser.GetMail(), loglist);//попытка достать объект итогов пользователя
        Itog[] itogs = new Itog[1];
        itogs[0]=myitog;
        try
        {
            CreateXLS._CreateXLS(itogs,xlspath,userlist);
        }
        catch (IOException ex){}
        catch (ClassNotFoundException ex) {}
    }
}