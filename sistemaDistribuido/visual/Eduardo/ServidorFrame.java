/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaDistribuido.visual.Eduardo;

import sistemaDistribuido.sistema.clienteServidor.Eduardo.ProcesoServidor;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

public class ServidorFrame extends ProcesoFrame{
  private static final long serialVersionUID=1;
  private ProcesoServidor proc;

  public ServidorFrame(MicroNucleoFrame frameNucleo){
    super(frameNucleo,"Servidor de Archivos Jose");
    proc=new ProcesoServidor(this);
    fijarProceso(proc);
  }
}