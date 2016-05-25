package br.com.sisac.view;

import java.io.File;
import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.CaptureEvent;

import br.com.sisac.model.Pessoa;
 
@ManagedBean
@SessionScoped
public class PhotoCamView {
     
    private String filename;
    private Pessoa visitante = new Pessoa();	
     
    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);
         
        return String.valueOf(i);
    }
 
    public String getFilename() {
        return filename;
    }
     
    public void oncapture(CaptureEvent captureEvent) {
        filename = getRandomImageName();
        byte[] data = captureEvent.getData();
 
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        String newFileName = externalContext.getRealPath("") + "resources" + File.separator + "visitante" + File.separator + filename + ".jpg";
        visitante.setImagem(newFileName);
         System.out.println("Caminho " + newFileName);
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        }
        catch(IOException e) {
            throw new FacesException("Erro ao Capiturar imagem.", e);
        }
    }
}