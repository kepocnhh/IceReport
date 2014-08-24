package icereport;

import api.API;
import api.CreatePDF;
import api.CreateXLS;
import api.SendEmail;
import com.itextpdf.text.DocumentException;
import ice.DataCass;
import ice.DataForRecord;
import ice.Itog;
import ice.Strings;
import ice.ping;
import ice.user;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.mail.MessagingException;

public class IceReport
{
    private static String userName = "iceandgoit@gmail.com";
    private static String password = "delaemicecreame";
    private static String pdfpath = "D:\\ForIce\\Reports\\";
    private static String xlspath = "D:\\ForIce\\XLS\\";
    private static String emailpath = "D:\\ForIce\\";
    private static String accpath = "D:\\ForIce\\IceServer\\accounts\\account";
    private static String logpath = "D:\\ForIce\\log\\";
    private static String version = "222";
    private static String toreg = "222";
    private static String pdfname = "PDFforIce";
    private static String xlsname = "XLSforIce";
    private static Strings myStrings;
    private static user myuser;
    private static Itog myitog;
    private static DataForRecord dfr;
    private static DataForRecord dfrdrug;
    private static DataForRecord dfrsteal;
    private static DataForRecord dfrclose;
    private static Date today;
    public static void main(String[] args)
    {
        DataCass[] cass = new DataCass[3];
        DataCass[] prom = new DataCass[3];
        DataCass[] inkd = new DataCass[3];
        try
        {
            SendEmail.SetProp(userName, password);
            CreatePDF.SetProp("D:\\ForIce\\M-R.otf");
            API.SetProp(accpath,logpath,version,toreg);
            myStrings = new Strings("D:\\ForIce\\DataForIce.txt");
            myuser = new user("Самолет\tТурбулентов\tЫыыевич\t4\tyulchik.blagonravova@mail.ru\t6\t7\tfalse");
            dfr = new DataForRecord(myStrings);
            dfr.nameshop="МАГАЗИН";
            dfr.setTypeEvent(DataForRecord.TypeEvent.open);
            dfrdrug = new DataForRecord(myStrings);
            dfrdrug.setTypeEvent(DataForRecord.TypeEvent.drug);
            dfrsteal = new DataForRecord(myStrings);
            dfrsteal.setTypeEvent(DataForRecord.TypeEvent.steal);
            dfrclose = new DataForRecord(myStrings);
            dfrclose.setTypeEvent(DataForRecord.TypeEvent.close);
            dfr.matrix[0][1]=3;
            dfrclose.matrix[0][1]=1;
            dfr.matrix[1][2]=6;
            dfrclose.matrix[1][2]=5;
            dfr.matrix[3][0]=2;
            dfrclose.matrix[3][0]=1;
            dfr.matrix[3][1]=9;
            dfrclose.matrix[3][1]=6;
            for(int i=0;i<cass.length;i++)
            {
                cass[i]=new DataCass(2, "", DataCass.TypeEvent.cass);
                prom[i]=new DataCass(2, "", DataCass.TypeEvent.promoter);
                inkd[i]=new DataCass(2+i, "ink"+i, DataCass.TypeEvent.inkasator);
            }
            today=new Date();
        }
        catch (IOException | DocumentException ex)
        {
            System.out.println(ex.toString());
            return;
        }
        try
        {
            pdfname = "PDFforIce_open(" +
                    today.getYear()+"-"+today.getMonth()+"-"+today.getDate()+"-"+
                    today.getHours()+"-"+today.getMinutes()+"-"+today.getSeconds()+")";
            today.setHours(8);
            today.setMinutes(43);
            ////////////////////////////////////////////////
            String file = 
                    "Благонравова 23.7.2014"
                    ;
            myuser = API.Find_user(accpath, myuser.GetMail());
            myitog = new Itog(API.Get_ping("open", logpath+"2014\\7\\23\\"+file).GetDate(),myuser.GetMail());
            myitog = API.Calculate_File_Itog(myitog, myuser, logpath+"2014\\7\\23\\"+file);
            ////////////////////////////////////////////////
            /*
            CreatePDF._CreatePDF(myStrings, myuser,
                    dfr,myitog,
                    pdfrath + pdfname);
            */
            /*
            SendEmail.sendPdf("kepocnhh@gmail.com", pdfname,
                                myitog.day_otw+"\n"+
                                "Начало рабочего дня "+myitog.date_open.getHours()+":"+CreatePDF.minutes(myitog.date_open.getMinutes()+"")
                                ,pdfrath + pdfname);
            */
            today=new Date();
            pdfname = "PDFforIce_close(" +
                    today.getYear()+"-"+today.getMonth()+"-"+today.getDate()+"-"+
                    today.getHours()+"-"+today.getMinutes()+"-"+today.getSeconds()+")";
            today.setHours(19);
            today.setMinutes(21);
            ////////////////////////////////////////////////
            dfr = API.Get_DFR(DataForRecord.TypeEvent.open, logpath+"2014\\7\\23\\"+file);
            dfrdrug = API.Get_DFR(DataForRecord.TypeEvent.drug, logpath+"2014\\7\\23\\"+file);
            dfrsteal = API.Get_DFR(DataForRecord.TypeEvent.steal, logpath+"2014\\7\\23\\"+file);
            dfrclose = API.Get_DFR(DataForRecord.TypeEvent.close, logpath+"2014\\7\\23\\"+file);
            if(dfrdrug==null)
            {
                dfrdrug = new DataForRecord(myStrings);
            }
            if(dfrsteal==null)
            {
                dfrsteal = new DataForRecord(myStrings);
            }
            CreatePDF._CreatePDF(myStrings, myuser,
                    dfr, dfrdrug, dfrsteal, dfrclose,
                    myitog,
                    pdfpath + pdfname);
            /*
            SendEmail.sendPdf("kepocnhh@gmail.com", pdfname,
                                myitog.day_otw+"\n"+
                                "Начало рабочего дня "+myitog.date_open.getHours()+":"+CreatePDF.minutes(myitog.date_open.getMinutes()+"")+"\n"+
                                "Конец рабочего дня "+myitog.date_close.getHours()+":"+CreatePDF.minutes(myitog.date_close.getMinutes()+"")+"\n"+
                                "--------------------"+"\n"+
                                "Продано кепок "+myitog.amount_k+"\n"+
                                "Выручка за кепки "+myitog.amount_k*myuser.price_k+"\n"+
                                "-"+"\n"+
                                "Продано стаканов "+myitog.amount_s+"\n"+
                                "Выручка за стаканы "+myitog.amount_s*myuser.price_s+"\n"+
                                "-"+"\n"+
                                "Всего выручка "+(myitog.amount_t*myuser.price_t+myitog.amount_k*myuser.price_k+myitog.amount_s*myuser.price_s)+"\n"+
                                "--------------------"+"\n"+
                                "Сумма бонуса "+myitog.amount_k*myuser.bonus+"\n"+
                                "--------------------"+"\n"+
                                "Вес кепок "+myitog.amount_k*myuser.weight_k+"\n"+
                                "Вес стаканов "+myitog.amount_s*myuser.weight_s+"\n"+
                                "-"+"\n"+
                                "Итого ВЕС "+(myitog.amount_t*myuser.weight_t+myitog.amount_k*myuser.weight_k+myitog.amount_s*myuser.weight_s)+"\n"+
                                "--------------------"+"\n"+
                                "Оклад "+myitog.salary+"\n"+
                                "ИТОГО ЗП "+(myitog.salary+myitog.amount_k*myuser.bonus)
                                ,pdfrath + pdfname);
            */
            today=new Date();
            xlsname = "XLSforIce(" +
                    today.getYear()+"-"+today.getMonth()+"-"+today.getDate()+"-"+
                    today.getHours()+"-"+today.getMinutes()+"-"+today.getSeconds()+")";
            Itog[] itogs = new Itog[1];
            itogs[0]=myitog;
            //CreateXLS._CreateXLS(itogs, accpath, xlspath+xlsname);
            CreateXLS._CreateXLS(new Date(2014,7,1), new Date(2014,7,25), xlspath+xlsname);
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }
}