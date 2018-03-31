package testes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLsx {



	public static void main(String[] args) throws FileNotFoundException, IOException{
		File xml = new File(args[0]);

		PrintWriter pw = new PrintWriter("TargetNTargets.xml");
		pw.write("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n<MCALI>\n");
		pw.flush();
		for (File fich : xml.listFiles()){
			InputStream ExcelFileToRead = new FileInputStream(args[0]+"\\"+fich.getName());
			System.out.println("Vai ler o ficheiro");
			XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
			System.out.println("Ja leu o ficheiro");

			for (int c = 0; c < 1; c++){
				XSSFSheet sheet = wb.getSheetAt(c);
				XSSFRow row; 
				XSSFCell cell;
				if (c % 2 == 0){
					pw.write("<Gesture Name='NTarget' NumStrokes='1' NumPts='256'>\n<Stroke ID='1'>\n");
					pw.flush();
				}else{
					pw.write("<Gesture Name='Target' NumStrokes='1' NumPts='256'>\n<Stroke ID='1'>\n");
					pw.flush();
				}

				Iterator rows = sheet.rowIterator();
				int a = 0;
				int b = 0;
				System.out.println("Ficheiro "+fich.getName());
				while (rows.hasNext())
				{
					row=(XSSFRow) rows.next();
					Iterator cells = row.cellIterator();

					while (cells.hasNext())
					{
						cell=(XSSFCell) cells.next();

						if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
						{
							System.out.print(cell.getStringCellValue()+" ");
						}
						else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
						{
							System.out.print(cell.getNumericCellValue()+" ");
							double s = cell.getNumericCellValue();
							int sds = (int) s;
							pw.write("<Point X='"+a+"' Y='"+(sds*5)+"' MS='0'/>\n");
							pw.flush();
						}
						else
						{
							//U Can Handel Boolean, Formula, Errors
						}
					}
					a++;
					b=b+6;

				}
				pw.write("</Stroke>\n</Gesture>\n");
				System.out.println();
				pw.flush();
				System.out.println("um ficheiro feito");
			}
			pw.write("</MCALI>");
			pw.flush();
			pw.close();
		}

	}

}
