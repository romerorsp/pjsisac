/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sisac.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author JAVA
 */
public abstract class Relatorio {
    
    	/** The jasper print. */
	private JasperPrint	jasperPrint;

	/**
	 * Controi o relatorio com os parametros recebidos.
	 *
	 * @param arquivoJasper the arquivo jasper
	 * @param parametros the parametros
	 * @param dirTemp the dir temp
	 * @return the jasper print
	 * @throws JRException the jR exception
	 */
	public JasperPrint criaRelatorio(String arquivoJasper, Map parametros) throws JRException
	{
            
            JRAbstractLRUVirtualizer virtualizer = new JRGzipVirtualizer(100);
		 
		//Seta o parametro REPORT_VIRTUALIZER com a instância da virtualização
		
		parametros.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObjectFromFile(arquivoJasper);
		 
		jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,new JRDataSource(){

					private final int	max		= 1;
					private int			cont	= 0;

					public Object getFieldValue(JRField jrField)
							throws JRException
					{
						
						return "main";
					}

					public boolean next() throws JRException
					{
						// TODO Auto-generated method stub
						if (cont++ >= max)
						{
							System.out.println("false");
							return false;
						} else
						{
							System.out.println("true");
							return true;
						}
					}
				});
               
		
		return jasperPrint;

	}
        
        
        
    public JasperPrint criaRelatorioDS(String arquivoJasper, Map parametros, JRDataSource ds) throws JRException{
           
           JRAbstractLRUVirtualizer virtualizer = new JRGzipVirtualizer(100);
                
               //Seta o parametro REPORT_VIRTUALIZER com a instância da virtualização
               
               parametros.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
               JasperReport jasperReport = (JasperReport) JRLoader
                               .loadObjectFromFile(arquivoJasper);
                
               jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,ds);
              
               
               return jasperPrint;

    }
        
    public JasperPrint criaRelatorioConn(String arquivoJasper, Map parametros, Connection conn) throws JRException, SQLException{
            
            JRAbstractLRUVirtualizer virtualizer = new JRGzipVirtualizer(100);
		 
		//Seta o parametro REPORT_VIRTUALIZER com a instância da virtualização
            
		
		parametros.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		JasperReport jasperReport;
                jasperReport = (JasperReport) JRLoader.loadObjectFromFile(arquivoJasper);

		 
		jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,conn);

               
		return jasperPrint;

    }

        

	/**
	 * Gets the jasper print.
	 *
	 * @return the jasper print
	 */
	public JasperPrint getJasperPrint()
	{
		return jasperPrint;
	}

	/**
	 * Sets the jasper print.
	 *
	 * @param jasperPrint the new jasper print
	 */
	public void setJasperPrint(JasperPrint jasperPrint)
	{
		this.jasperPrint = jasperPrint;
	}
        
        
        
}
