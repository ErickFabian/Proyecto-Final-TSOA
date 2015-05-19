/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

/**
 *
 * @author Fabian Garcia Erick
 * Practica 4
 * Clase Auxiliar que almacena id e ip para registro en programa conector
 * 
 */
public class Datos implements ParMaquinaProceso {

    int id = 0;
    String ip = "";
    
    public Datos(int id, String ip){
        this.id = id;
        this.ip = ip;
    }
    
    public String getIp() {
        return ip;
    }

    public int getId() {
        return id;
    }

    @Override
    public String dameIP() {
        return ip;
    }

    @Override
    public int dameID() {
        return id;
    }
}
