/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sistemaDistribuido.visual.clienteServidor;

/**
 *
 * @author Fabian Garcia Erick Alfonso
 * Practica 5
 * Interfaz grafica Nueva que realiza la funcion del servidor de Nombres y despliega las acciones y los servidores en ejecucion usando como base el programa conector de practica 4
 */

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ConectorDNS;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Hashtable;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Puente;
import java.awt.TextArea;
import sistemaDistribuido.visual.rpc.DespleganteConexiones;

/**
* 
*/
class PanelConexion extends Panel{
  private static final long serialVersionUID=1;
  private Checkbox seleccion;
  private int id;
  private String nombreServidor;
  /**
  * 
  */
  public PanelConexion(Checkbox seleccion,String nombreServidor,String version,String ip,String id,int identificacionUnica){
    Panel panelDescripcion=new Panel();
    this.seleccion=seleccion;
    this.id=identificacionUnica;
    this.nombreServidor=nombreServidor;
    setLayout(new BorderLayout());
    panelDescripcion.setLayout(new GridLayout(1,4));
    panelDescripcion.add(new Label(nombreServidor,Label.LEFT));
    panelDescripcion.add(new Label(version,Label.LEFT));
    panelDescripcion.add(new Label(ip,Label.LEFT));
    panelDescripcion.add(new Label(id,Label.LEFT));
    add(BorderLayout.WEST,seleccion);
    add(BorderLayout.CENTER,panelDescripcion);
  }
  
  /**
  * 
  */
  public Checkbox dameCheckbox(){
    return seleccion;
  }
  
  /**
  * 
  */
  public int dameIdPanel(){
    return id;
  }
  
  public String dameNombre(){
      return  nombreServidor;
  }
}

public class ServidorNombres extends Frame implements DespleganteConexiones,WindowListener{
  private static final long serialVersionUID=1;
  private MicroNucleoFrame frameNucleo;
  private Panel panelConexiones=new Panel();
  private Panel panelNotificaciones=new Panel();
  private CheckboxGroup grupoConexiones=new CheckboxGroup();
  private Hashtable<Integer,PanelConexion> tablaConexiones=new Hashtable<Integer,PanelConexion>();
  private Hashtable<Checkbox,PanelConexion> tablaCheckbox=new Hashtable<Checkbox,PanelConexion>();
  private GridLayout layout;
  private String agregarStr="Agregar";
  private String removerStr="Remover de conector";
  private TextField campoNombre,campoVersion,campoIP,campoID;
  private Puente conector;
  private int claveEntrada=0;
  final TextArea notificacionesTextArea = new TextArea("",5,30);
  /**
  * 
  */
  public ServidorNombres(MicroNucleoFrame frameNucleo){
    super("Servidor de Nombres");
    this.frameNucleo=frameNucleo;
    panelConexiones.setLayout(layout=new GridLayout(1,1));
    add(BorderLayout.NORTH,construirPanelNorte());
    add(BorderLayout.CENTER,panelConexiones);
    
    add(BorderLayout.SOUTH,construirPanelSur());
    setSize(500,350);
    addWindowListener(this);
    conector=new Puente(this);
    ConectorDNS.asignarConector(conector);
  }
  
  /**
  * 
  */
  private Panel construirPanelNorte(){
    Panel panelNorte=new Panel();
    Panel panelDescripcion=new Panel();
    panelNorte.setLayout(new BorderLayout());
    panelDescripcion.setLayout(new GridLayout(1,4));
    panelDescripcion.add(new Label("SERVIDOR",Label.LEFT));
    panelDescripcion.add(new Label("VERSION",Label.LEFT));
    panelDescripcion.add(new Label("ASA IP",Label.LEFT));
    panelDescripcion.add(new Label("ASA ID",Label.LEFT));
    panelNorte.add(BorderLayout.WEST,new Label("  "));
    panelNorte.add(BorderLayout.CENTER,panelDescripcion);
    return panelNorte;
  }
  
  /**
  * 
  */
  private Panel construirPanelSur(){
    Panel panelSur=new Panel();
    Panel panelCampos=new Panel();
    
    panelSur.setLayout(new GridLayout(2,1));
    panelCampos.setLayout(new BorderLayout());
    Panel p=new Panel();
    p.setLayout(new GridLayout(1,4));
    panelCampos.add(BorderLayout.WEST,new Label("  "));
    panelCampos.add(BorderLayout.CENTER,p);
    panelSur.add(panelCampos);
    panelSur.add(notificacionesTextArea);
    return panelSur;
  }

  
  
  /**
  * 
  */
  private int agregar(String nombreServidor,String version,String ip,String id){
    Checkbox check=new Checkbox("",true,grupoConexiones);
    PanelConexion p=new PanelConexion(check,nombreServidor,version,ip,id,++claveEntrada);
    Integer clave=new Integer(claveEntrada);
    int cant=panelConexiones.getComponentCount();
    tablaConexiones.put(clave,p);
    tablaCheckbox.put(check,p);
    if (cant>0){
      layout.setRows(cant+1);
    }
    panelConexiones.add(p);
    panelConexiones.validate();
    validate();
    notificacionesTextArea.append("Servidor "+nombreServidor+" Agregado, "+"ID: "+id+" , IP: "+ip+"\n");
    return claveEntrada;
  }
  
  /**
  * 
  */
  private void remover(PanelConexion p){
    int cant=panelConexiones.getComponentCount();
    panelConexiones.remove(p);
    if (cant>1){
      layout.setRows(cant-1);
    }
    panelConexiones.validate();
    validate();
    notificacionesTextArea.append("Servidor Cerrado"+"\n");
  }
  
  /**
  * 
  */
  public int agregarServidor(String nombreServidor,String version,String ip,String idProceso){
    return agregar(nombreServidor,version,ip,idProceso);
  }
  
  /**
  * 
  */
  public void removerServidor(int llaveInterfaz){
    PanelConexion p=(PanelConexion)tablaConexiones.remove(new Integer(llaveInterfaz));
    if (p!=null){
      tablaCheckbox.remove(p.dameCheckbox());
      remover(p);
    }
  }
  
  /**
  * 
  */
  public void finalizar() {
    frameNucleo.cerrarFrameConector();
  }
  
  /**
  * 
  */
  public void windowClosing(WindowEvent e) {
    conector.terminar();
  }
  
  public void windowDeactivated(WindowEvent e) {
  }
  public void windowClosed(WindowEvent e) {
  
    
  }
  public void windowDeiconified(WindowEvent e) {
  }
  public void windowOpened(WindowEvent e) {
  }
  public void windowIconified(WindowEvent e) {
  }
  public void windowActivated(WindowEvent e) {
  }
}
