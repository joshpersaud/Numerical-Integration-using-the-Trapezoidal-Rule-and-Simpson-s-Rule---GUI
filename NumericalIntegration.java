package numericalintegration;

import static javafx.application.Application.launch;
import com.mathworks.engine.*;
import com.mathworks.matlab.types.HandleObject;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import java.util.Arrays;
import java.util.List;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NumericalIntegration {

    /**
     * @param args the command line arguments
     */
    static Future<MatlabEngine> eng;
    static MatlabEngine ml;
    static GUI gui;
    static boolean endthread=false;
    static JLabel load;
    static JFrame Splash;
    static String path= "MatlabFunctions/Josh_Functions/";
    public static void main(String[] args) throws EngineException, InterruptedException, MatlabSyntaxException, ExecutionException {
        gui = new GUI();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(System.getProperty("java.library.path"));
        Splash = new JFrame();
        load = new JLabel("LOADING");
        
        Splash.add(load);
        Splash.setSize(400,100);
        load.setLocation(Splash.getSize().width/2-load.getSize().width/2, Splash.getSize().height/2-load.getSize().height/2);
        Splash.setLocation(dim.width/2-Splash.getSize().width/2, dim.height/2-Splash.getSize().height/2);
        Splash.setUndecorated(true);
        
        
        
        
        gui.setLocation(dim.width/2-gui.getSize().width/2, dim.height/2-gui.getSize().height/2);
        startt1();
        eng = MatlabEngine.startMatlabAsync();
        ml = eng.get();
        
        // Get engine instance      
          // Evaluate the command to cd to your function
        ml.eval("cd "+path);
        endthread=true;
        System.out.println("LOADING DONE.");
        
        gui.setTitle("Numerical Integration");
        /*
        int answer=ml.feval("twoplusX", 2);
        System.out.println(answer);
        */
        gui.setVisible(true);
    }
    
    public static void compute(int i)
    {
        double []x = gui.X;
        double []y = gui.Y;
        //System.out.println("Computing: " + x[i]);
        
        // Evaluate the command to cd to your function
        String pointx = arrayForMatlab(x);
        String pointy = arrayForMatlab(y);
        //y=ml.feval("LagrangeInterpolation", pointx, pointy, x);
        try {
                double answer =ml.feval("LagrangeInterpolation", gui.Xold, gui.Yold, x[i]);
                gui.Y[i] = answer;
            } catch (RejectedExecutionException ex) {
                Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
    }
    public static String TrapezoidalRule(){
        Writer output;
            output = new StringWriter();
        try {
            String funct = "@(x)" + gui.f;
            ml.eval("f = "+funct, new StringWriter(), new StringWriter());
            
            
            ml.putVariableAsync("X", gui.X);
            ml.putVariableAsync("Y", gui.Y);
            
            
            
            //ml.evalAsync("TrapezoidalRule(f, X)");
            
            //HandleObject h = ml.feval("str2func", funct);
            //Object feval = ml.eval("TrapezoidalRule", output, ml.feval("str2func", fun).toString(), gui.X);
            //ml.evalAsync("f");
            
            
            ml.eval("TrapezoidalRule(f, X)",output, output);
            //System.out.println(output.toString());
            
            ml.eval("Y");

        } catch (RejectedExecutionException ex) {
            Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output.toString();
    }
    public static String Simpson(){
        Writer output;
            output = new StringWriter();
        try {
            String funct = "@(x)" + gui.f;
            ml.eval("f = "+funct, new StringWriter(), new StringWriter());
            
            
            ml.putVariableAsync("X", gui.X);
            ml.putVariableAsync("Y", gui.Y);
            
            //ml.evalAsync("TrapezoidalRule(f, X)");
            
            //HandleObject h = ml.feval("str2func", funct);
            //Object feval = ml.eval("TrapezoidalRule", output, ml.feval("str2func", fun).toString(), gui.X);
            //ml.evalAsync("f");
            
            
            ml.eval("SimpsonRule(f, X)",output, output);
            //System.out.println(output.toString());
            
            

        } catch (RejectedExecutionException ex) {
            Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(NumericalIntegration.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output.toString();
    }
    static public void endThread()
    {
        endthread=true;
    }

    public static String arrayForMatlab(double []X)
    {
        String s = "[ ";
        for(int i = 0; i < X.length;i++)
        {
            s += X[i]+" ";
        }
        s+="]";
        return s;
    }
    public static void close() throws EngineException
    {
        ml.close();
        ml.disconnectAsync();
        eng.cancel(true);
    }

    
    public static double[] strtodouble(String[] s)
    {
        double[] d = new double[s.length];
        for(int i = 0; i < d.length; i++)
        {
            d[i] = Double.parseDouble(s[i]);
        }
        return d;
    }
    public static void startt1()
    {
        Thread t1 = new Thread(){
            @Override
            public void run()
            {
                String loading= "LOADING";
                long iteration = 100000000;
                long i =0;
                Splash.setVisible(true);
                gui.setEnabled(false);
                while(!endthread)
                {
                    
                    if(i==iteration){
                    if(loading.length()<12)
                    {
                        loading+='.';
                        
                    }
                    else
                    {
                        loading="LOADING";
                    }
                    load.setText(loading);
                    load.updateUI();
                    i=0;
                    }
                    else
                    {
                        i++;
                        continue;
                    }
                }
                float opacity=1;
                i=0;
                
                
                while(true)
                {
                    
                    if(i==iteration){
                        
                        opacity-=.001;
                        if(opacity<0)
                        {
                            endthread=false;
                
                            gui.setEnabled(true);

                            gui.toFront();
                            gui.repaint();
                            Splash.setVisible(false);
                            Splash.setOpacity(1);
                            break;
                        }
                        Splash.setOpacity(opacity);
                        Splash.repaint();
                        Splash.toFront();
                    }
                    else
                    {
                        i++;
                        continue;
                    }
                }
                
            }
        };
        t1.start();
    }
}
