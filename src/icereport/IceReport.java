package icereport;

import api.*;
import ice.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;

public class IceReport
{
    static private List<String> acc_file_path;
    static private List<String> StringList;
    static private List<user> UserList;
    static private String mail;
    static private String newpath;
    static private String new_accpath;
    static private String xlspath;
    static private String xlsname;
    static private Date bgn;
    static private Date end;
        static private void linux_deb()
        {
            new_accpath = "/home/toha/ForIce/account_new";
            newpath = "/home/toha/ForIce/NewLogs/";
            xlspath = "/home/toha/ForIce/newXLS";
        }
        static private void windows_deb()
        {
            //new_accpath = "D:\\Downloads\\ForIce\\NewAccounts\\account";
            new_accpath = "D:\\Downloads\\ForIce\\NewAccounts";
            newpath = "D:\\Downloads\\ForIce\\Logs\\NewLogs\\";
            xlspath = "D:\\Downloads\\ForIce\\XLS";
            xlsname = "newXLS";
            mail = "iceandgoit@gmail.com";
            bgn = new Date(114, 9, 1);
            end = new Date(114, 9, 22);
            StringList = null;
            UserList = new ArrayList();
            String[] MailList = mail.split(" ");
            SendEmail.SetProp("iceandgoit@gmail.com", "delaemicecreame", MailList);
            System.out.println("SendEmail.SetProp good");
        }
    static public int SetProp(String[] args)
    {
        if(args.length!=1 && args.length!=3)
        {
            System.out.println("args[0] - path port file");
            System.out.println("or");
            System.out.println("args[0] - path port file");
            System.out.println("args[1] - date begin (14-1-1)");
            System.out.println("args[2] - date end (14-1-2)");
            return -1;
        }
        List<String> al = GetArgsList(args[0]);
        String db = "";
        String de = "";
        if(args.length == 1)
        {
            if(al.size() != 9)
            {
                System.out.println("in port file" + "\n" +
                        "args[1] - accounts path" + "\n" +
                        "args[2] - log path" + "\n" +
                        "args[3] - xls path" + "\n" +
                        "args[4] - mail list" + "\n" +
                        "args[5] - your mail data" + "\n" +
                        "args[6] - xls name" + "\n" +
                        "args[7] - date begin" + "\n" +
                        "args[8] - date end"
                        );
                return -1;
            }
            db = al.get(4);
            de = al.get(5);
        }
        if(args.length == 3)
        {
            if(al.size() != 7)
            {
                System.out.println("in port file" + "\n" +
                        "args[1] - accounts path" + "\n" +
                        "args[2] - log path" + "\n" +
                        "args[3] - xls path" + "\n" +
                        "args[4] - mail list" + "\n" +
                        "args[5] - your mail data" + "\n" +
                        "args[6] - xls name"
                        );
                return -1;
            }
            db = args[1];
            de = args[2];
        }
            SetProp(al.get(1), al.get(2), al.get(3), al.get(6));
            System.out.println("IceReport.SetProp good");
            SendEmail.SetProp(al.get(5).split(" ")[0], al.get(5).split(" ")[1], al.get(4).split(" "));
            System.out.println("SendEmail.SetProp good");
            return SetDates(db, de);
    }
    static public void SetProp(String a,String p,String x,String xn)
    {
        new_accpath = a;
        newpath = p;
        xlspath = x;
            xlsname = xn;
        System.out.println(
                new_accpath + " - accounts path" + "\n" +
                newpath + " - log path" + "\n" +
                xlspath + " - xls path" + "\n" +
                xlsname + " - xls name"
                );
    }
    static public int SetDates(String b,String e)
    {
        if(b.split("-").length!=3)
        {
            System.out.println("Begin " + b + " error");
            System.out.println("Begin length " + b.split(" ").length);
            return -1;
        }
        if(e.split("-").length!=3)
        {
            System.out.println("End " + e + " error");
            System.out.println("End length " + e.split(" ").length);
            return -1;
        }
            bgn = new Date();
            bgn.setYear(Integer.parseInt(b.split("-")[0])-1900);
            bgn.setMonth(Integer.parseInt(b.split("-")[1])-1);
            bgn.setDate(Integer.parseInt(b.split("-")[2]));
            end = new Date();
            end.setYear(Integer.parseInt(e.split("-")[0])-1900);
            end.setMonth(Integer.parseInt(e.split("-")[1])-1);
            end.setDate(Integer.parseInt(e.split("-")[2]));
            int days = 0;
            Date tmp = (Date) bgn.clone();
            while(tmp.before(end))
            {
                days++;
                tmp.setDate(tmp.getDate()+1);
            }
            return days;
    }
    static private List<String> GetArgsList(String args)
    {
        List<String> al = null;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(args));
            String str;
            al = new ArrayList<String>();
            while ((str = br.readLine()) != null)
            {
                al.add(str);
            }
        }
        catch (FileNotFoundException ex)
        {
        }
        catch (IOException ex)
        {
        }
        return al;
    }
    static private List<String> GetAccFilesList(String args)
    {
        List<String> al = null;
        String[] files = new File(args).list();
            al = new ArrayList<>();
            for(int i=0; i<files.length; i++)
            {
                if(files[i].lastIndexOf("account") != -1)
                {
                    al.add(files[i]);
                }
            }
        return al;
    }
    static private int Change_SnU_List(Date d, int n)
    {
        int fn = 0;
        for(int i=0;i<acc_file_path.size();i++)
        {
            String sd = acc_file_path.get(i).split("_")[1];
            Date tmpd = new Date();
            tmpd.setYear(Integer.parseInt(sd.split("-")[0])-1900);
            tmpd.setMonth(Integer.parseInt(sd.split("-")[1])-1);
            tmpd.setDate(Integer.parseInt(sd.split("-")[2]));
            if(d.after(tmpd))
            {
                fn = i;
            }
        }
        return fn;
    }
    static public Itog[] recurs_addItog(String ph, String[] fs, Itog[] itgs, List<String> sl)
    {
                if(fs == null)
                {
                    System.out.println("fs == null"+" :(");
                    bgn.setDate(bgn.getDate()+1);
                    return itgs;
                }
                for(int i=0; i<fs.length;i++)
                {
                    if(fs[i].equals("photo")||fs[i].equals("pdf"))
                    {
                        continue;
                    }
                    if(fs[i].equals("supers"))
                    {
                        String path2= ph + "supers"+"/";
                        String[] files_supers = new File(newpath+path2).list();
                        itgs = recurs_addItog(path2, files_supers, itgs, sl);
                        continue;
                    }
                    if(fs[i].endsWith(".ice"))
                    {
                        itgs = addItog(ph, fs[i], itgs, sl);
                        continue;
                    }
                }
                return itgs;
    }
    static public Itog[] addItog(String p, String f, Itog[] itgs, List<String> sl)
    {
        try {
            FileInputStream fis = new FileInputStream(newpath+p+f);
            ObjectInputStream read = new ObjectInputStream(fis);
                        List<BaseMessage> list_BM = (List) read.readObject();
            fis.close();
            read.close();
            Itog newitog = null;
            for(int q=0;q<list_BM.size();q++)
            {
                if(list_BM.get(q).getClass()==Itog.class)
                {
                    newitog = (Itog) list_BM.get(q);
                }
            }
            if(newitog.date_close==null)
            {
                return itgs;
            }
            //user newuser = API.Get_user(newitog.user_email, sl);
            Itog[] tmp = new Itog[itgs.length+1];
            for(int q=0;q<itgs.length;q++)
            {
                tmp[q] = itgs[q];
            }
            tmp[itgs.length] = newitog;
            itgs = tmp;
            //System.out.println(p+f);
        } catch (IOException ex) {
            System.out.println(f+" "+ex.getMessage());
                return itgs;
        } catch (ClassNotFoundException ex) {
            System.out.println(f+" "+ex.getMessage());
                return itgs;
        }
                return itgs;
    }
    static public void Create_Send_XLS(Itog[] itogs, int act_n)
    {
                System.out.println("bgn " + bgn.toString());
                System.out.println("end " + end.toString());
        if(itogs.length<=0)
        {
                System.out.println("itogs.length<=0"+" :(");
            return;
        }
        String xls_path = xlspath+"/"+xlsname+"_"+ acc_file_path.get(act_n).split("_")[1];
        try
        {
            CreateXLS._CreateXLS(itogs, xls_path,UserList);
                System.out.println("CreateXLS " + xls_path+".xls"+ " DONE");
            for (String mails : SendEmail.maillist)//отправляем письмо с отчётом и коментарием всем адресам в списке SendEmail.maillist
            {
                SendEmail.sendFile(
                        mails,
                        "Отчёт от "+acc_file_path.get(act_n).split("_")[1], //Тема сообщения
                        "",
                        xls_path+".xls",
                        xlsname+"_"+ acc_file_path.get(act_n).split("_")[1]+".xls");
                System.out.println("XLS "+xlsname+"_"+ acc_file_path.get(act_n).split("_")[1]+".xls "+" send to " + mails);
            }
        }
        catch (IOException | ClassNotFoundException | MessagingException ex)
        {
                System.out.println(ex.getMessage());
        }
    }
    public static void main(String[] args)
    {
        //linux_deb();
        //windows_deb();
            int days = 0;
            days = SetProp(args);
            if(days>=0)
            {
                System.out.println("IceReport.SetDates good\n");
            }
            else
            {
                System.out.println("IceReport.SetDates error !!!");
                return;
            }
            System.out.println("Begin - " + (bgn.getYear()+1900) +" year " + (bgn.getMonth()+1) +" month "+ bgn.getDate() +" day");
            System.out.println("End - " + (end.getYear()+1900) +" year " + (end.getMonth()+1) +" month "+ end.getDate() +" day");
                System.out.println(days + " days");
        acc_file_path = GetAccFilesList(new_accpath);
        Collections.sort(acc_file_path);
        //System.out.println(acc_file_path);
        Itog[] itogs = new Itog[0];
        double temp_days = 0;
                int act_n = 0;
            int temp_n = act_n;
            StringList = null;
            UserList = new ArrayList();
            try
            {
                StringList = API.Get_String_List(new_accpath+"/"+acc_file_path.get(act_n)); //список с данными пользователей
            }
            catch (IOException | ClassNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
                System.out.println("\n");
            for(int i=0; i<StringList.size(); i++)
            {
                UserList.add(new user(StringList.get(i)));
                System.out.println(UserList.get(i).GetMail());
            }
                System.out.println("\n0 %");
            while(bgn.before(end))
            {
                temp_n = Change_SnU_List(bgn,act_n);
                if(temp_n != act_n)
                {
                    Create_Send_XLS(itogs,act_n);
                    act_n = temp_n;
                    itogs = new Itog[0];
                    UserList = new ArrayList();
                    try
                    {
                        StringList = API.Get_String_List(new_accpath+"/"+acc_file_path.get(act_n)); //список с данными пользователей
                    }
                    catch (IOException | ClassNotFoundException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                    for(int i=0; i<StringList.size(); i++)
                    {
                        UserList.add(new user(StringList.get(i)));
                        System.out.println(UserList.get(i).GetMail());
                    }
                }
                temp_days++;
                String path = (bgn.getYear()+1900)+"/"+(bgn.getMonth()+1)+"/"+bgn.getDate()+"/";
                String[] files = new File(newpath+path).list();
                        itogs = recurs_addItog(path, files, itogs, StringList);
                bgn.setDate(bgn.getDate()+1);
                System.out.println((int)(temp_days/days*100)+" %");
            }
                    Create_Send_XLS(itogs,act_n);
    }
}