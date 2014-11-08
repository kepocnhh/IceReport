package icereport;

import api.API;
import api.CreateXLS;
import ice.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        Itog myitog;//объект итогов пользователя
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
            while(bgn.before(end))
            {
                String path = (bgn.getYear()+1900)+"/"+(bgn.getMonth()+1)+"/"+bgn.getDate()+"/";
                String[] files = new File(newpath+path).list();
                if(files == null)
                {
                                   System.out.println(path+" - "+"files == null");
                    bgn.setDate(bgn.getDate()+1);
                    continue;
                }
                for(int i=0; i<files.length;i++)
                {
                    if(files[i].equals("photo")||files[i].equals("pdf"))
                    {
                                   System.out.println(path+" - "+files[i]);
                        continue;
                    }
                    if(files[i].equals("supers"))
                    {
                        String path2= path + "supers"+"/";
                        String[] files_supers = new File(newpath+path2).list();
                        for(int j=0; j<files_supers.length;j++)
                        {
                            String superpth= path2+ files_supers[j]+"/";
                            String[] _super = new File(newpath+superpth).list();
                            for(int k=0; k<_super.length;k++)
                            {
                                if(_super[k].equals("photo")||_super[k].equals("pdf"))
                                {
                                   System.out.println(superpth+" - "+_super[k]);
                                    continue;
                                }
                                if(_super[k].endsWith(".ice"))
                                {
                                    try {
                                        FileInputStream fis = new FileInputStream(newpath+superpth+_super[k]);
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
                                        user newuser = API.Get_user(newitog.user_email, stringlist);
                                        Itog[] tmp = new Itog[itogs.length+1];
                                        for(int q=0;q<itogs.length;q++)
                                        {
                                            tmp[q] = itogs[q];
                                        }
                                        tmp[itogs.length] = newitog;
                                        itogs = tmp;
                                        System.out.println(superpth+_super[k]);
                                    } catch (IOException ex) {
                                        System.out.println(_super[k]+" "+ex.getMessage());
                                        continue;
                                    } catch (ClassNotFoundException ex) {
                                        System.out.println(_super[k]+" "+ex.getMessage());
                                        continue;
                                    }
                                    continue;
                                }
                                   System.out.println(superpth+" - "+"another file");
                                    continue;
                            }
                        }
                    }
                                if(files[i].endsWith(".ice"))
                                {
                                    try {
                                        FileInputStream fis = new FileInputStream(newpath+path+files[i]);
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
                                        user newuser = API.Get_user(newitog.user_email, stringlist);
                                        Itog[] tmp = new Itog[itogs.length+1];
                                        for(int q=0;q<itogs.length;q++)
                                        {
                                            tmp[q] = itogs[q];
                                        }
                                        tmp[itogs.length] = newitog;
                                        itogs = tmp;
                                        System.out.println(path+files[i]);
                                    } catch (IOException ex) {
                                        System.out.println(files[i]+" "+ex.getMessage());
                                        continue;
                                    } catch (ClassNotFoundException ex) {
                                        System.out.println(files[i]+" "+ex.getMessage());
                                        continue;
                                    }
                                    continue;
                                }
                                   System.out.println(path+" - "+"another file");
                }
                bgn.setDate(bgn.getDate()+1);
            }
        try
        {
            CreateXLS._CreateXLS(itogs,xlspath,userlist);
        }
        catch (IOException ex){}
        catch (ClassNotFoundException ex) {}
    }
}