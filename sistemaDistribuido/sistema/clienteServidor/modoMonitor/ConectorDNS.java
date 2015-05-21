/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Puente;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;   //para pr�ctica 4
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

/**
 *
 * @author Fabian Garcia Erick Alfonso
 * Practica 5
 * Clase Nueva creada como conector hacia el servidor de nombres usando una cadena como parametro de busqueda
 */
public class ConectorDNS {
        private static Puente conector;

	public static void asignarConector(Puente con){
		conector=con;
		conector.inicializar();
	}

	public static int importarInterfaz(String nombreServidor){
		    System.out.println(nombreServidor);
            ParMaquinaProceso asa=conector.busqueda(nombreServidor);
            System.out.println(asa.dameID());
            return asa.dameID();
	}

	public static int exportarInterfaz(String nombreServidor,String version,ParMaquinaProceso asa){
            return conector.registro(nombreServidor,version,asa);
	}

	public static boolean deregistrarInterfaz(String nombreServidor,String version,int identificacionUnica){
            return conector.deregistrar(nombreServidor,version,identificacionUnica);
	}
}
