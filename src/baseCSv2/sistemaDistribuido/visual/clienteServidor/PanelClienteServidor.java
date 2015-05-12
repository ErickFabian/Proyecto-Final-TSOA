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
  private Button botonCliente,botonServidor, botonDNS;

  public PanelClienteServidor(){
     botonCliente=new Button("Cliente");
     botonServidor=new Button("Servidor");
     botonDNS = new Button("Servidor de Nombres");
     add(botonCliente);
     add(botonServidor);
     add(botonDNS);
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
  
  public void agregarActionListener(ActionListener al){
    botonCliente.addActionListener(al);
    botonServidor.addActionListener(al);
    botonDNS.addActionListener(al);
  }
}