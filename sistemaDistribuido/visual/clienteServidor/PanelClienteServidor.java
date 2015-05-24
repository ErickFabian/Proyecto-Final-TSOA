package sistemaDistribuido.visual.clienteServidor;

import java.awt.Panel;
import java.awt.Button;
import java.awt.event.ActionListener;

/**
 *
 * @author Fabian Garcia Erick Alfonso
 * Practica 5
 * Agrega boton de Servidor de Nombres usado en microNucleoFrame asi como su action listener
 */

public class PanelClienteServidor extends Panel{
  private static final long serialVersionUID=1;
  private Button botonCliente,botonServidor, botonDNS,alexs,alexc, eduardos, eduardoc;

  public PanelClienteServidor(){
     botonCliente=new Button("Cliente Erick");
     botonServidor=new Button("Servidor Erick");
     alexs=new Button("Servidor Alex");
     alexc=new Button("Cliente Alex");
     eduardos=new Button("Servidor Eduardo");
     eduardoc=new Button("Cliente Eduardo");
     botonDNS = new Button("Servidor de Nombres");
     add(botonCliente);
     add(botonServidor);
     add(botonDNS);
     add(alexc);
     add(alexs);
     add(eduardoc);
     add(eduardos);
  }
  
  public Button dameBotonCliente(){
    return botonCliente;
  }
  
  public Button dameBotonServidor(){
    return botonServidor;
  }
  
  public Button dameBotonServidorNombres(){
    return botonDNS;
  }
  
  public Button dameBotnClienteAlex()
  {
   return alexc;
  }
  
  public Button dameBotnServidorAlex()
  {
   return alexs;
  }
  
  public Button dameBotnClienteEduardo()
  {
   return eduardoc;
  }
  
  public Button dameBotnServidorEduardo()
  {
   return eduardos;
  }
  
  public void agregarActionListener(ActionListener al){
    botonCliente.addActionListener(al);
    botonServidor.addActionListener(al);
    botonDNS.addActionListener(al);
    alexs.addActionListener(al);
    alexc.addActionListener(al);
    eduardos.addActionListener(al);
    eduardoc.addActionListener(al);
  }
}