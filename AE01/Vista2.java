package AE01;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ImageIcon;

public class Vista2 extends JFrame {

	JPanel contentPane;
	JTextField textField;
	JButton btnBuscar;
	JButton btnCopiar;
	DefaultListModel<Object> listModel;
	static JList<Object> list; 
	JScrollPane scrollPane;
	JTextArea textContingut;
	JButton btnCrear;
	JButton btnCanviarN;
	JButton btnBuscarO;
	JButton btnEditar;
	JButton btnReemplazar;
	JButton btnGuardar;
	JButton btnSuprimir;
	String ruta=""; // On s'emmagatzema la ruta del directori
	static boolean activado = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vista2 frame = new Vista2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Vista2() {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 549);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(279, 22, 431, 34);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnBuscar = new JButton("Buscar Fitxer");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ruta=abrir();
				textField.setText(ruta);
				Refrescar();
			}
		});
		btnBuscar.setBounds(26, 276, 185, 34);
		contentPane.add(btnBuscar);
		
		btnCrear = new JButton("Crear Fitxer");
		btnCrear.setEnabled(false);
		btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crearFitxer(ruta);
				textField.setText("");
				textContingut.setText("");
				Refrescar();
			}
		});
		btnCrear.setBounds(26, 321, 185, 34);
		contentPane.add(btnCrear);
		
		btnCanviarN = new JButton("Canviar Nom");
		btnCanviarN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cambiarNombre(ruta);
				Refrescar();
			}
		});
		btnCanviarN.setEnabled(false);
		btnCanviarN.setBounds(26, 366, 185, 34);
		contentPane.add(btnCanviarN);
		
		btnCopiar = new JButton("Copiar Fitxer");
		btnCopiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(copyFile(ruta+"/"+list.getSelectedValue()) == true) {
					JOptionPane.showMessageDialog(null, "Fitxer copiat correctament");
				}
				Refrescar();
			}
		});
		btnCopiar.setEnabled(false);
		btnCopiar.setBounds(26, 411, 185, 34);
		contentPane.add(btnCopiar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setIcon(null);
		btnEditar.setEnabled(false);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(activado == false) {
					textContingut.setEnabled(true);;
					activado = true;
				}else {
					textContingut.setEnabled(false);
					activado = false;
				}
			}
		});
		btnEditar.setBounds(235, 427, 89, 61);
		contentPane.add(btnEditar);
		
		btnBuscarO = new JButton("Buscar");
		btnBuscarO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField buscar= new JTextField();
	    		
	    		Object[] fields = {
	    				"Palabra a buscar: ", buscar
	    		};   		
	    		int opcion=JOptionPane.showConfirmDialog(null,fields,"Buscar paraula",JOptionPane.OK_CANCEL_OPTION);
	    		if(opcion == JOptionPane.OK_OPTION) {
	    			int contador=buscarpalabra(textContingut,buscar.getText());
	    			JOptionPane.showMessageDialog(null,"Numero de coincidencies: "+contador);
	    		}
	    		
			}
		});
		btnBuscarO.setEnabled(false);
		btnBuscarO.setBounds(357, 427, 89, 61);
		contentPane.add(btnBuscarO);
		
		btnReemplazar = new JButton("Reemplaçar");
		btnReemplazar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField buscar= new JTextField();
				JTextField remplazar= new JTextField();
	    		Object[] fields = {
	    				"Palabra a remplazar: ", buscar,
	    				"Palabra nueva: ", remplazar
	    		};   		
	    		int opcion=JOptionPane.showConfirmDialog(null,fields,"Remplaçar paraula",JOptionPane.OK_CANCEL_OPTION);
	    		if(opcion == JOptionPane.OK_OPTION) {
    			int contador=reemplazarPalabras(buscar.getText(),remplazar.getText(),textContingut);
    			JOptionPane.showMessageDialog(null,"Total de coincidencies canviades: "+contador);
	    		}
	    		
			}
		});
		btnReemplazar.setEnabled(false);
		btnReemplazar.setBounds(480, 427, 89, 61);
		contentPane.add(btnReemplazar);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resp =JOptionPane.showConfirmDialog(null,"SEGUR QUE VOLS GUARDAR?", "GUARDAR",
			               JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
				guardar(textField.getText(),resp);
				try {
					leer(ruta+"/"+list.getSelectedValue());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				textField.setText("");
				textContingut.setText("");
				Refrescar();
			}
		});
		btnGuardar.setEnabled(false);
		btnGuardar.setBounds(609, 427, 89, 61);
		contentPane.add(btnGuardar);
		
		btnSuprimir = new JButton("Suprimir Fitxer");
		btnSuprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resp =JOptionPane.showConfirmDialog(null,"SEGUR QUE VOLS ELIMINAR?", "ELIMINAR",
			               JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
				eliminar(ruta+"/"+list.getSelectedValue().toString(),resp);
				textField.setText("");
				textContingut.setText("");
				Refrescar();
				}
		});
		btnSuprimir.setEnabled(false);
		btnSuprimir.setBounds(26, 456, 185, 34);
		contentPane.add(btnSuprimir);
		
		JLabel lblNewLabel = new JLabel("Ruta:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(225, 32, 46, 14);
		contentPane.add(lblNewLabel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 22, 185, 233);
		contentPane.add(scrollPane);
		
		list = new JList<Object>();
		scrollPane.setViewportView(list);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(221, 68, 490, 346);
		contentPane.add(scrollPane_1);
		
		textContingut = new JTextArea();
		textContingut.setEnabled(false);
		scrollPane_1.setViewportView(textContingut);

	}
	
	
	/**
	 * Obrix un selector de directoris on pots tria una carpeta de treball
	 * @return retorna la adressa del directori seleccionat
	 */
	public static String abrir() {
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        f.showSaveDialog(null);
        return f.getSelectedFile().toString();
    }
	
	/**
	 * Carrega un array amb els arxius del directori seleccionat
	 * @param fa referencia a un parametre de tipus File que conté la ruta del directori
	 * @return un array de strings pasats per un filtre d'extensió
	 */
	public static String[] cargararchivos(File arc) {
        Filtro filtro=new Filtro(".txt");
        return arc.list(filtro);
    }
	
	/**
	 * Carrega una lista dels arxius del directori a un objecte Jlist
	 * @param arch, ruta del directori
	 */
	public void cargarLista(String arch) {
		File archivo = new File(arch);
        String[] Lista= cargararchivos(archivo);
        list = new JList<Object>();
        scrollPane.setViewportView(list);

        listModel = new DefaultListModel<Object>();
        for (int i=0; i<Lista.length; i++) {
            listModel.addElement(Lista[i]);
        }
        list.setModel(listModel);
    }
	
	/**
	 * Llegix el contingut d'un fitxer txt
	 * @param ruta, ruta del arxiu
	 * @return String amb tot el contingut del fitxer
	 */
	public static String leer(String ruta) throws IOException {
        String currentLine;
        String texto="";
        try {
             BufferedReader reader = new BufferedReader(new FileReader(ruta));
             while((currentLine = reader.readLine()) != null) {
                texto+=currentLine+"\n";
             }

             reader.close();

         }catch (FileNotFoundException e){
             System.err.println("Error al llegir.");
         }
        return texto;
    }
	
	/**
	 * Obrix el contingut del fitxer seleccionat per index i el mostra en el textArea, també mostra la ruta en el textField
	 * @param arch, ruta del directori
	 */
	public void abrirArchivo(String arch){
		File archivo = new File(arch);
        list.addMouseListener((MouseListener) new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                list = (JList)evt.getSource();
                if (evt.getClickCount() == 1) {
                    int index = list.locationToIndex(evt.getPoint());
                    try {
                        textContingut.setText(leer(archivo+"/"+list.getSelectedValue()));
                        textField.setText(archivo+ "\\" + list.getSelectedValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    comprobar();
                    System.out.println("index: "+index);
                } 
            }
        });
    }
	
	/**
	 * Crea un fitxer depenent del nom proporcionat
	 * @param arch, ruta del directori
	 * @return String de la ruta completa del arxiu nou
	 */
	public static String crearFitxer(String arch) {
		String ruta = "";
		JTextField nombre= new JTextField();
		try {
			Object[] fields = {
					"Nom arxiu: ", nombre
			};  
			int opcion=JOptionPane.showConfirmDialog(null,fields,"Nom arxiu",JOptionPane.OK_CANCEL_OPTION);
			File fichero = new File (arch + "/" + nombre.getText());
			if (!fichero.exists()) {
	    		if(opcion == JOptionPane.OK_OPTION) {
	    			if (fichero.createNewFile()) {
						JOptionPane.showMessageDialog(null, "Fitxer creat correctament");
					}
					else {
						JOptionPane.showMessageDialog(null, "Ha hagut un problema al crear el fitxer");
					}
	    		}	
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		ruta = (arch + "/" + nombre.getText());
		return ruta;
	}
    
    /**
     * Elimina fitxers
     * @param ruta, ruta completa del arxiu
     * @param opcion, opcio que se li dona a elegir al usuari anteriormente 0 o 1
     */
    public static void eliminar(String ruta,int opcion) {
		File dirfit = new File(ruta);
		
		if(opcion==0) {
			if (dirfit.exists()) {
				  if (dirfit.delete()) {
					  JOptionPane.showMessageDialog(null,"Fitxer s'ha eliminat correctament");
				  }
				  else {
					  JOptionPane.showMessageDialog(null,"Ha hagut un problema al eliminar el fitxer");  
				  }
				}else {
					JOptionPane.showMessageDialog(null,"El fitxer no existeix");
				}
		}
	}
    
    /**
     * Cambia el nom d'un arxiu txt, també es pot cambiar la seua extensió
     * @param ruta, ruta del directori
     */
    public static void cambiarNombre(String ruta) {
    	File fichero = new File(ruta+"/"+list.getSelectedValue());
    	if(fichero.exists()) {
    		JTextField nuevo= new JTextField();
    		
    		Object[] fields = {
    				"Nombre nuevo: ", nuevo
    		};   		
    		int opcion=JOptionPane.showConfirmDialog(null,fields,"Canviar nom",JOptionPane.OK_CANCEL_OPTION);
    		if(opcion == JOptionPane.OK_OPTION) {
    			File nombre = new File(ruta+"/"+nuevo.getText());
    			if(fichero.renameTo(nombre)) {
    				 JOptionPane.showMessageDialog(null,"Nom canviat exitosament");
    			}else{
    				JOptionPane.showMessageDialog(null,"Error al cambiar");
    			};
    		}		
		}
    }
    
    /**
     * Copia el fitxer elegit sobre el mateix directori
     * @param toFile, ruta completa del fitxer
     * @return true o false, solament per a comprobar si s'ha creat o no
     */
    public boolean copyFile(String toFile) {
    	String destinacion = toFile.replace(".txt", "");
        File origin = new File(toFile);
        File destination = new File(destinacion + "_copy.txt");
        if (origin.exists()) {
        	if(!destination.exists()) {
        		try {
	                InputStream in = new FileInputStream(origin);
	                OutputStream out = new FileOutputStream(destination);
	                
	                byte[] buf = new byte[1024];
	                int len;
	                while ((len = in.read(buf)) > 0) {
	                    out.write(buf, 0, len);
	                }
	                in.close();
	                out.close();
	                return true;
	            } catch (IOException ioe) {
	                ioe.printStackTrace();
	                JOptionPane.showMessageDialog(null, "Error");
	                return false;
	            }
        	}else {
        		JOptionPane.showMessageDialog(null, "El fitxer ja existeix");
        		return false;
        	}
        } else {
        	return false;
        }
    }
    
    /**
     * Reemplaça les paraules seleccionades per altres
     * @param palabra, paraula a buscar per a reemplaçar
     * @param reemplazo, paraula que reemplaça 
     * @param area1, textArea on está el text
     * @return contador de coincidencies
     */
    public int reemplazarPalabras(String palabra, String reemplazo, JTextArea area1) {
    	String text = area1.getText();
    	String nuevo=text.replaceAll(palabra, reemplazo);
    	int cont=0;
    	Pattern p = Pattern.compile("(?i)" + palabra);
        Matcher m = p.matcher(text);
        while (m.find()) {
            cont++;
        }
    	textContingut.setText(nuevo);
    	return cont;
    }
	
    /**
     * Busca la paraula seleccionada en el textArea
     * @param area1, textArea on está el text
     * @param texto, text que vols buscar
     * @return contador de coincidencies
     */
	public int buscarpalabra(JTextArea area1, String texto) {
		int cont=0;
        if (texto.length() >= 1) {
            DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
            Highlighter h = area1.getHighlighter();
            h.removeAllHighlights();
            String text = area1.getText();
            String caracteres = texto;
            Pattern p = Pattern.compile("(?i)" + caracteres);
            Matcher m = p.matcher(text);
            while (m.find()) {
                try {
                    h.addHighlight(m.start(), m.end(), highlightPainter);
                    cont++;
                } catch (BadLocationException ex) {
                    
                }
            }
        } else {
            JOptionPane.showMessageDialog(area1, "la paraula a buscar no pot estar buida");
        }
        return cont;
    }
	
	/**
	 * Al pasar el ratolí per d'amunt del arxiu txt mostrara tota la informació requerida
	 * @param arch, ruta del directori
	 */
	public void datos(String arch) {
		File archivo = new File(arch);
        list.addMouseMotionListener((MouseMotionListener) new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JList<?> l = (JList<?>)e.getSource();
                ListModel<?> m = l.getModel();
                int index = l.locationToIndex(e.getPoint());
                if( index>-1 ) {
                    File arg=new File(archivo+"/"+m.getElementAt(index).toString());
                    double tamanyo=arg.length()/1000;
                    BasicFileAttributes attrs;
                    String formatted="";
                    try {
                        attrs = Files.readAttributes(arg.toPath(), BasicFileAttributes.class);
                        FileTime time = attrs.creationTime();
                        String pattern = "yyyy-MM-dd HH:mm:ss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        formatted = simpleDateFormat.format( new Date( time.toMillis() ) );

                    } catch (IOException exc) {
                        exc.printStackTrace();

                    }

                    l.setToolTipText("<html>" + "Nom: "+m.getElementAt(index).toString() + "<br>" + "Tamanyo:"+tamanyo+"KB" + "<br>" +
                    "Data creació: " + formatted +"<br>"+"Ruta: "+arg.getAbsolutePath()+"<br>"+ "<br>"+"Executable: " + arg.canExecute()+"<br>"+"Readable: "+arg.canRead()+
                    "<br>"+"Writable: "+ arg.canWrite()+"</html>");
                }
            }
        });
    }
	
	/**
	 * Dona a elegir si vol guardar l'arxiu en altre nou o sobreescrivir-o sobre el mateix
	 * @param arch, ruta del fitxer txt
	 * @param opcion, int 0 o 1 d'un showOptionDialog anterior
	 */
	public void guardar(String arch,int opcion) {
		if(opcion==0) {
			int resp =JOptionPane.showOptionDialog(null,"GUARDAR EN EL MISMO O UNO NUEVO?", "GUARDAR",
		               JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, new Object[] { "sobreescriure", "nou" }, null);
			
				if(resp == 0) {
					try {
						BufferedWriter bw=new BufferedWriter(new FileWriter(arch));
						bw.write(textContingut.getText().toString());
						bw.close();
						JOptionPane.showMessageDialog(null,"Guardat exitosament");
					}catch(IOException e)
					{
						JOptionPane.showMessageDialog(null,"Error al guardar");
					}
				}else {
					try {
						BufferedWriter bw=new BufferedWriter(new FileWriter(crearFitxer(ruta)));
						bw.write(textContingut.getText().toString());
						bw.close();
					}catch(IOException e)
					{
					}
				}
			try {
				leer(ruta+"/"+list.getSelectedValue());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Depenent de si un arxiu está seleccionat o no activara els botons
	 */
	public void comprobar() {
		if(list.getSelectedIndex()==-1||textField.getText().equals("")) {
			btnCanviarN.setEnabled(false);
			btnSuprimir.setEnabled(false);
			btnCopiar.setEnabled(false);
			btnEditar.setEnabled(false);
			btnGuardar.setEnabled(false);
			btnBuscarO.setEnabled(false);
			btnReemplazar.setEnabled(false);
		}else {
			btnCrear.setEnabled(true);
			btnCanviarN.setEnabled(true);
			btnSuprimir.setEnabled(true);
			btnCopiar.setEnabled(true);
			btnEditar.setEnabled(true);
			if(textContingut.getText().equals("")) {
				btnBuscarO.setEnabled(false);
				btnReemplazar.setEnabled(false);
				btnGuardar.setEnabled(true);
			}else {
				btnBuscarO.setEnabled(true);
				btnReemplazar.setEnabled(true);
				btnGuardar.setEnabled(true);
			}
		}
	}
	
	/**
	 * Refresca el programa utilitçant metodes anteriors
	 */
	public void Refrescar() {
		if(textField.getText() == "") {
			btnCrear.setEnabled(false);
			btnCanviarN.setEnabled(false);
			btnSuprimir.setEnabled(false);
			btnCopiar.setEnabled(false);
			btnEditar.setEnabled(false);
			btnGuardar.setEnabled(false);
			btnBuscarO.setEnabled(false);
			btnReemplazar.setEnabled(false);
		}else {
			btnCrear.setEnabled(true);
			btnCanviarN.setEnabled(true);
			btnSuprimir.setEnabled(true);
			btnCopiar.setEnabled(true);
			btnEditar.setEnabled(true);
			btnGuardar.setEnabled(true);
			btnBuscarO.setEnabled(true);
			btnReemplazar.setEnabled(true);
		}
		comprobar();
		cargarLista(ruta);
		abrirArchivo(ruta);
		datos(ruta);
	}
}
