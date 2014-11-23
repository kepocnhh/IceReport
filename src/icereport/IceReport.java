package icereport;

import api.API;
import api.CreateXLS;
import ice.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IceReport
{
    static private String newpath;
    static private String new_accpath;
    static private String xlspath;
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
                new_accpath = "D:\\Downloads\\ForIce\\NewAccounts\\account";
                newpath = "D:\\Downloads\\ForIce\\Logs\\NewLogs\\";
                xlspath = "D:\\Downloads\\ForIce\\newXLS";
            }
    static public void SetProp(String a,String p,String x)
    {
                new_accpath = a;
                newpath = p;
                xlspath = x;
    }
    static public int SetDates(String b,String e)
    {
        if(b.split(" ").length!=3)
        {
            System.out.println("Begin " + b + " error");
            System.out.println("Begin length " + b.split(" ").length);
            return -1;
        }
        if(e.split(" ").length!=3)
        {
            System.out.println("End " + e + " error");
            System.out.println("End length " + e.split(" ").length);
            return -1;
        }
            bgn = new Date();
            bgn.setYear(Integer.parseInt(b.split(" ")[0])-1900);
            bgn.setMonth(Integer.parseInt(b.split(" ")[1])-1);
            bgn.setDate(Integer.parseInt(b.split(" ")[2]));
            end = new Date();
            end.setYear(Integer.parseInt(e.split(" ")[0])-1900);
            end.setMonth(Integer.parseInt(e.split(" ")[1])-1);
            end.setDate(Integer.parseInt(e.split(" ")[2]));
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
    static public Itog[] recurs_addItog(String ph, String[] fs, Itog[] itgs, List<String> sl)
    {
                if(fs == null)
                {
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
                                        user newuser = API.Get_user(newitog.user_email, sl);
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
    public static void main(String[] args)
    {
        //linux_deb();
        //windows_deb();
        if(args.length!=1)
        {
            System.out.println("args[0] - path port file");
            return;
        }
        List<String> al = GetArgsList(args[0]);
        if(al.size() != 6)
        {
            System.out.println("in port file" + "\n" +
                    "args[1] - accounts path" + "\n" +
                    "args[2] - log path" + "\n" +
                    "args[3] - xls path" + "\n" +
                    "args[4] - date begin" + "\n" +
                    "args[5] - date end"
                    );
            return;
        }
            SetProp(al.get(1), al.get(2), al.get(3));
            System.out.println("IceReport.SetProp good");
            int days = SetDates(al.get(4), al.get(5));
            if(days>=0)
            {
                System.out.println("IceReport.SetDates good");
            }
            else
            {
                System.out.println("IceReport.SetDates error !!!");
                return;
            }
            
            System.out.println("Begin - " + (bgn.getYear()+1900) +" year " + (bgn.getMonth()+1) +" month "+ bgn.getDate() +" day");
            System.out.println("End - " + (end.getYear()+1900) +" year " + (end.getMonth()+1) +" month "+ end.getDate() +" day");

                System.out.println(days + " days");
        List<String> stringlist = null;
        List<user> userlist = new ArrayList();
        try
        {
            stringlist = API.Get_String_List(new_accpath); //список с данными пользователей
        }
        catch (IOException ex){}
        catch (ClassNotFoundException ex) {}
            for (String bmlist1 : stringlist)
            {
                userlist.add(new user(bmlist1));
            }
        Itog[] itogs = new Itog[0];
        double temp_days = 0;
                System.out.println("0 %");
            while(bgn.before(end))
            {
                temp_days++;
                String path = (bgn.getYear()+1900)+"/"+(bgn.getMonth()+1)+"/"+bgn.getDate()+"/";
                String[] files = new File(newpath+path).list();
                        itogs = recurs_addItog(path, files, itogs, stringlist);
                bgn.setDate(bgn.getDate()+1);
                System.out.println((int)(temp_days/days*100)+" %");
            }
        try
        {
            CreateXLS._CreateXLS(itogs,xlspath,userlist);
                System.out.println("CreateXLS " + xlspath+".xls" + " DONE");
        }
        catch (IOException ex){}
        catch (ClassNotFoundException ex) {}
    }
}