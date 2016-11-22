/**
 * The following code, with exceptions of some names and the FileIn,
 * was taken from http://www.rgagnon.com/javadetails/java-print-a-text-file-with-javax.print-api.html
 * 
 * This class prints out a file, the name of which is passed to the constructor
 * from the main program.
 */
package catpuccinocoffee;

import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.Copies;
import javax.print.event.*;


public class Printing 
{
	Printing(String Name) throws PrintException, IOException
	{
		String DefaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
		PrintService Service = PrintServiceLookup.lookupDefaultPrintService();
		FileInputStream FileIn = new FileInputStream(new File(Name));
		PrintRequestAttributeSet PRAS = new HashPrintRequestAttributeSet();
		PRAS.add(new Copies(1));
		
		DocFlavor Flav = DocFlavor.INPUT_STREAM.AUTOSENSE;
		Doc doc = new SimpleDoc(FileIn, Flav, null);
		DocPrintJob Job = Service.createPrintJob();
		PrintJobWatcher Watcher = new PrintJobWatcher(Job);
		Job.print(doc, PRAS);
		Watcher.waitForDone();
		FileIn.close();
		
		InputStream ff = new ByteArrayInputStream("\f".getBytes());
		Doc docff = new SimpleDoc(ff, Flav, null);
	    DocPrintJob jobff = Service.createPrintJob();
	    Watcher = new PrintJobWatcher(jobff);
	    jobff.print(docff, null);
	    Watcher.waitForDone();
	}
	
	class PrintJobWatcher 
	{
		boolean done = false;

		PrintJobWatcher(DocPrintJob job) 
		{
			job.addPrintJobListener(new PrintJobAdapter() 
			{
				public void printJobCanceled(PrintJobEvent pje) 
				{
					allDone();
				}
				public void printJobCompleted(PrintJobEvent pje) 
				{
					allDone();
				}
				public void printJobFailed(PrintJobEvent pje) 
				{
					allDone();
				}
				public void printJobNoMoreEvents(PrintJobEvent pje)
				{
					allDone();
				}
				void allDone()
				{
					synchronized (PrintJobWatcher.this) 
					{
						done = true;
			          System.out.println("Printing done ...");
					   PrintJobWatcher.this.notify();
					}
				}
			});
		}
		public synchronized void waitForDone() 
		{
			try 
			{
				while (!done) 
				{
					wait();
				}
			} 
			catch (InterruptedException e) 
			{
			}
		}
	}
}
