/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package statistics;

import environment.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelViewController.CommonSens;
import sensing.Sensor;

/**
 *
 * @author jarleso
 */
public final class PlotCreator {

        public static void createGnuplotLoadMeasurements(int numRuns,
                long numSecs, int numSens, String expPrefix) {

        BufferedWriter outGnu = null;
        BufferedWriter outMeasurement = null;

        try {
            outGnu = new BufferedWriter(new FileWriter(expPrefix +
                    "/measurements.dem"));

            outGnu.write("set terminal postscript eps enhanced \"Times-Roman\"\n"
                + "set terminal postscript eps enhanced \"NimbusSanL-Regu\"\n"
                + "set term postscript eps enhanced\n"
                + "set output \"sensors_pulled.eps\"\n"
                + "plot ");

            HashMap<Long, Measure> measureMap =
                    new HashMap<Long, Measure>();

            // Read through all the files and make average caluculations.

            for(int i = 0; i < numRuns; i++) {

                Scanner sc = new Scanner(new File(expPrefix + i +
                        "_Measurements.dat"));

                while(sc.hasNext()) {

                    long time = Long.valueOf(sc.next());
                    double batchTime = Double.valueOf(sc.next());
                    double batchSize = Double.valueOf(sc.next());
                    double evaluateBatch = Double.valueOf(sc.next());
                    double queuePulling = Double.valueOf(sc.next());
                    double stateEvaluation = Double.valueOf(sc.next());
                    double getTimeTupleSourcesToPull = Double.valueOf(sc.next());
                    double getTupleSourcesToPull = Double.valueOf(sc.next());

                    if(!measureMap.containsKey(time)) {

//                        System.err.println("Putting: " + time + " " + batchTime + " " +
//                                batchSize + " " + evaluateBatch + " " + queuePulling + " " +
//                                stateEvaluation + " " + getTupleSourcesToPull);

                        measureMap.put(time, new Measure(time, batchTime,
                                batchSize, evaluateBatch, queuePulling,
                                stateEvaluation, getTimeTupleSourcesToPull,
                                getTupleSourcesToPull));
                    } else {

//                        System.err.println("Adding: " + batchTime + " " +
//                                batchSize + " " + evaluateBatch + " " + queuePulling + " " +
//                                stateEvaluation + " " + getTupleSourcesToPull);

                        measureMap.get(time).addLine(batchTime, batchSize,
                                evaluateBatch, queuePulling,
                                stateEvaluation, getTimeTupleSourcesToPull,
                                getTupleSourcesToPull);
                    }
                }
            }

            // Write measureMap to file.

            outMeasurement = new BufferedWriter(new FileWriter(expPrefix +
                    "/measurements_calculated.dat"));

            System.err.println("Size of measureMap: " + measureMap.size());

            for(long i = 0; i < measureMap.size(); i++) {

                if(measureMap.containsKey(i)) {

                    System.err.println("Writing to file: " +
                            measureMap.get(i).getLine());

                    outMeasurement.write(measureMap.get(i).getLine() + "\n");

                }
            }

            outMeasurement.close();

            outGnu.write("'measurements_calculated.dat' using 5:6:7 with yerrorbars title 'batchSize', " +
                    "'measurements_calculated.dat' using 8:9:10 with yerrorbars title 'evaluateBatch', " +
                    "'measurements_calculated.dat' using 11:12:13 with yerrorbars title 'queuePulling', " +
                    "'measurements_calculated.dat' using 14:15:16 with yerrorbars title 'stateEvaluation', " +
                    "'measurements_calculated.dat' using 17:18:19 with yerrorbars title 'getTupleSourcesToPull', " +
                    "'measurements_calculated.dat' using 17:18:19 with yerrorbars title 'getTupleSourcesToPull', " +
                    "'measurements_calculated.dat' using 20:21:22 with yerrorbars title 'numberOfTupleSourcesToPull'");

            outGnu.close();

        } catch (IOException ex) {
            Logger.getLogger(CommonSens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createGnuplotTupleProcessing3D(int numRuns, long numSecs, int numSens,
            int numAnds, String expPrefix) {

        BufferedWriter outGnu = null;
        BufferedWriter outMeasurement = null;

        try {
            outGnu = new BufferedWriter(new FileWriter(expPrefix + "/measurements_"
                    + numRuns + "_" + numSecs + "_" + numSens + "_"
                    + numAnds + ".dem"));

            outGnu.write("set terminal postscript eps enhanced \"Times-Roman\"\n"
                + "set terminal postscript eps enhanced \"NimbusSanL-Regu\"\n"
                + "set term postscript eps enhanced\n"
                + "set output \"tupleProcessing_" + numRuns + "_" + numSecs + "_" + numSens + "_"
                    + numAnds + ".eps\"\n"
                + "set xlabel " + ((numAnds > 0) ? "\"Number of concurrent amotic statements\"" : "\"Number of sensors\"") + "\n"
                + "set zlabel \"Time (milliseconds)\"\n"
                + "set ylabel \"Time steps\"\n"
                + "#set xtics 1\n"
                + "#set ztics 1\n"
                + "set hidden3d\n"
                + "#set dgrid3d\n"
                + "splot ");

            HashMap<Integer, ArrayList<HashMap<Long, Measure>>> andHash =
                    new HashMap<Integer, ArrayList<HashMap<Long, Measure>>>();


            // Read through all the files and make average caluculations.

            for(int a = 0; a <= numAnds * 10; a += 10) {

                // For each of the number of sensors, we create hashmap in an arraylist.

                andHash.put(a, new ArrayList<HashMap<Long, Measure>>());

                for(int y = 0; y < numSens; y++) {

                    System.err.println("Adding to measureMap " + y);

                    andHash.get(a).add(y, new HashMap<Long, Measure>());

                    for(int i = 0; i < numRuns; i++) {

                        Scanner sc = new Scanner(new File(expPrefix + i + "_" + (y * 10) + "_" + a + "_Measurements.dat"));

                        // Read the current file.

                        while(sc.hasNext()) {

                            long time = Long.valueOf(sc.next());
                            double batchTime = Double.valueOf(sc.next());
                            double batchSize = Double.valueOf(sc.next());
                            double evaluateBatch = Double.valueOf(sc.next());
                            double queuePulling = Double.valueOf(sc.next());
                            double stateEvaluation = Double.valueOf(sc.next());
                            double getTimeTupleSourcesToPull = Double.valueOf(sc.next());
                            double getTupleSourcesToPull = Double.valueOf(sc.next());

                            if(!andHash.get(a).get(y).containsKey(time)) {

                                System.err.println("Putting: " + time + " " + batchTime + " " +
                                        batchSize + " " + evaluateBatch + " " + queuePulling + " " +
                                        stateEvaluation + " " + getTupleSourcesToPull);

                                andHash.get(a).get(y).put(time, new Measure(time, batchTime,
                                        batchSize, evaluateBatch, queuePulling,
                                        stateEvaluation, getTimeTupleSourcesToPull,
                                        getTupleSourcesToPull));

                            } else {

                                System.err.println("Adding: " + batchTime + " " +
                                        batchSize + " " + evaluateBatch + " " + queuePulling + " " +
                                        stateEvaluation + " " + getTupleSourcesToPull);

                                andHash.get(a).get(y).get(time).addLine(batchTime, batchSize,
                                        evaluateBatch, queuePulling,
                                        stateEvaluation, getTimeTupleSourcesToPull,
                                        getTupleSourcesToPull);
                            }
                        }
                    }
                }
            }

            // Write measureMap to file.

            outMeasurement = new BufferedWriter(new FileWriter(expPrefix + "/TupleProcessing3D_" + numRuns + "_" + numSecs + "_" + numSens + "_"
                    + numAnds + ".dat"));

            for(int a = 0; a <= numAnds * 10; a += 10) {

                // This gives the X values.

                System.err.println("Size of measureMap: " + andHash.get(a).size());

                for(int x = 0; x < andHash.get(a).size(); x++) {

                    System.err.println("measureMaps.get(x).size() = " + andHash.get(a).get(x).size());

                    for(long z = 0; z < andHash.get(a).get(x).size(); z++) {

                        if(andHash.get(a).get(x).containsKey(z)) {

                            outMeasurement.write(((x * 10) + 1) + "\t" + a + "\t"
                                    + andHash.get(a).get(x).get(z).getLine() + "\n");
                        }
                    }

                    // Add newline to make gnuplot better.

                    outMeasurement.write("\n");
                }
            }

            outMeasurement.close();

            outGnu.write("'TupleProcessing3D_" + numRuns + "_" + numSecs + "_" + numSens + "_"
                    + numAnds + ".dat' u " +
                    ((numAnds > 0)? "2:3:10" : "1:3:10") + " with lines notitle\n");

            outGnu.write("pause -1");

            outGnu.close();

        } catch (IOException ex) {
            Logger.getLogger(CommonSens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createGnuplotTupleProcessing2D(int numRuns, long numSecs, int numSens,
            int numAnds, String expPrefix) {

        BufferedWriter outGnu = null;
        BufferedWriter [] outMeasurement = null;

        try {
            outGnu = new BufferedWriter(new FileWriter(expPrefix + "/TupleProcessing2D_measurements_"
                    + numRuns + "_" + numSecs + "_" + numSens + "_"
                    + numAnds + ".dem"));

            outGnu.write(
                "set term postscript eps enhanced\n"
                + "set output \"tupleProcessing_" + numRuns + "_" + numSecs + "_" + numSens + "_"
                    + numAnds + ".eps\"\n"
                + "set ylabel \"Time (milliseconds)\"\n"
                + "set xlabel \"Time steps\"\n"
                + "#set xrange[:12]\n"
                + "#set xtics 1\n"
                + "#set ztics 1\n"
                + "plot ");

            HashMap<Integer, ArrayList<HashMap<Long, Measure>>> andHash =
                    new HashMap<Integer, ArrayList<HashMap<Long, Measure>>>();


            /**
             * Read through all the files and make average caluculations. This
             * is done through an object of the Measure class.
             */

            for(int a = 0; a <= numAnds * 10; a += 10) {

                // For each of the number of sensors, we create hashmap in an arraylist.

                andHash.put(a, new ArrayList<HashMap<Long, Measure>>());

                for(int y = 0; y < numSens; y++) {

                    System.err.println("Adding to measureMap " + y);

                    andHash.get(a).add(y, new HashMap<Long, Measure>());

                    for(int i = 0; i < numRuns; i++) {

                        Scanner sc = new Scanner(new File(expPrefix + i +
                                "_" + (y * 10) + "_" + a + "_Measurements.dat"));

                        // Read the current file.

                        while(sc.hasNext()) {

                            long time = Long.valueOf(sc.next());
                            double batchTime = Double.valueOf(sc.next());
                            double batchSize = Double.valueOf(sc.next());
                            double evaluateBatch = Double.valueOf(sc.next());
                            double queuePulling = Double.valueOf(sc.next());
                            double stateEvaluation = Double.valueOf(sc.next());
                            double getTimeTupleSourcesToPull = Double.valueOf(sc.next());
                            double getTupleSourcesToPull = Double.valueOf(sc.next());

                            if(!andHash.get(a).get(y).containsKey(time)) {

                                System.err.println("Putting: " + time + " " + batchTime + " " +
                                        batchSize + " " + evaluateBatch + " " + queuePulling + " " +
                                        stateEvaluation + " " + getTupleSourcesToPull);

                                andHash.get(a).get(y).put(time, new Measure(time, batchTime,
                                        batchSize, evaluateBatch, queuePulling,
                                        stateEvaluation, getTimeTupleSourcesToPull,
                                        getTupleSourcesToPull));

                            } else {

                                System.err.println("Adding: " + batchTime + " " +
                                        batchSize + " " + evaluateBatch + " " + queuePulling + " " +
                                        stateEvaluation + " " + getTupleSourcesToPull);

                                andHash.get(a).get(y).get(time).addLine(batchTime, batchSize,
                                        evaluateBatch, queuePulling,
                                        stateEvaluation, getTimeTupleSourcesToPull,
                                        getTupleSourcesToPull);
                            }
                        }
                    }
                }
            }

            /**
             * Write measureMap to file. If the andHash is greater than 1,
             * the measurements are done based on increasing the number of
             * ANDs. Otherwise, the number of LoIs has increased. This is then
             * indicated in the HashMap in andHash[0].
             */

            int itSize = 0;

            if(andHash.size() > 1) {
                outMeasurement = new BufferedWriter[andHash.size()];

                itSize = andHash.size();
            } else {
                outMeasurement = new BufferedWriter[andHash.get(0).size()];

                itSize = andHash.get(0).size();
            }

            for(int i = 0; i < itSize; i++) {

                outMeasurement[i] = new BufferedWriter(new FileWriter(expPrefix + "/TupleProcessing2D_" + numRuns + "_" + numSecs + "_" + numSens + "_"
                    + numAnds + "_" + i + ".dat"));
            }

            int outIterator = 0;

            for(int a = 0; a <= numAnds * 10; a += 10) {

                // This gives the X values.

                System.err.println("Size of measureMap: " + andHash.get(a).size());

                for(int x = 0; x < andHash.get(a).size(); x++) {

                    System.err.println("andHash.get(" + a + ").get(" + x + 
                    		").size() = " + andHash.get(a).get(x).size());
                    
                    /**
                     * Debug: run through the keys.
                     */
                    
//                    for(long tmp : andHash.get(a).get(x).keySet()) {
//                    	
//                    	System.err.println("key = " + tmp);
//                    	
//                    }
                    
                    /**
                     * Note that we have to increase z with 1000 since we are now using milliseconds.
                     */
                    
                    for(long z = 0; z < andHash.get(a).get(x).size()*1000; z += 1000) {
                    	
//                    	System.err.println("Checking if z = " + z + " is key.");
                    	
                        if(andHash.get(a).get(x).containsKey(z)) {

//                        	System.err.println("yes");
                        	
                            outMeasurement[outIterator].write(((x * 10) + 1) + "\t" + a + "\t"
                                    + andHash.get(a).get(x).get(z).getLine() + "\n");
                        } else {
//                        	System.err.println("no");
                        }
                    }

                    outGnu.write("\"TupleProcessing2D_" + numRuns +
                            "_" + numSecs + "_" + numSens + "_" + numAnds +
                            "_" + outIterator + ".dat\" u ($3/1000):10 with histeps ti " +
                            ((numAnds > 0) ? "\"" + ((a == 0)?
                                "1 atomic query\"" : (a + 1) + " atomic queries\"") :
                                "\"Experiment #" + (x + 1) + "\""));

                    System.err.println("x = " + x + 
                            ", (andHash.get(a).size() - 1) = " +
                            (andHash.get(a).size() - 1));

                    if(((numAnds > 0) && a < (numAnds * 10)) ||
                            x < (andHash.get(a).size() - 1)) {
                        outGnu.write(", ");
                    }

                    /**
                     * Increase iterator. Since the iterator either iterates
                     * through numAnds or numSens, it has to be updated
                     * independently of the HashMap/ArrayList.
                     */

                    outIterator += 1;
                }
            }

            for(int i = 0; i < itSize; i++) {

                System.err.println("Closing " + i);

                outMeasurement[i].close();
            }

            outGnu.write("\npause -1");

            outGnu.close();

        } catch (IOException ex) {
            Logger.getLogger(CommonSens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createGnuplotPulledSensors(int numRuns, long numSecs,
            String expPrefix, ArrayList<Environment> currEnvs) {

        try {
            BufferedWriter outGnu = null;

            outGnu = new BufferedWriter(new FileWriter(expPrefix + "/pulled_sensors.dem"));

//            outGnu.write("set terminal postscript eps enhanced \"Times-Roman\\n"
//                    + "set terminal postscript eps enhanced \"NimbusSanL-Regu\"\n"
//                    + "set term postscript eps enhanced\n"
//                    + "set output \"sensors_pulled.eps\"\n" + "set xrange[0.5:]\n" + "set yrange[0.5:10.5]\n" + "set size ratio 0.5\n" + "set xlabel \"Time\"\n" + "set grid\n" + "set pointsize 2\n");

//            outGnu.write("set ytics (");

            outGnu.write("set terminal postscript eps enhanced \"Times-Roman\"\n"
                    + "set terminal postscript eps enhanced \"NimbusSanL-Regu\"\n"
                    + "set term postscript eps enhanced\n"
                    + "set output \"sensors_pulled.eps\"\n"
                    + "plot ");

            ArrayList<Sensor> tmpSensors = currEnvs.get(0).getTupleSources();

            for(int i = 0; i < tmpSensors.size(); i++) {

                Sensor tmpSens = tmpSensors.get(i);

//                outGnu.write("\"" + tmpSens.getName() + "\"" + (i + 1));

//                if(i < tmpSensors.size() - 1) {
//                    outGnu.write(",");
//                }

                // Calculate average for each sensor and create a file with the results.

                HashMap<Integer, Integer> avgCounter = new HashMap<Integer, Integer>();

                int maxLength = 0;

                for(int y = 0; y < numRuns; y++) {

                    Scanner tmpSc = new Scanner(new File(expPrefix + tmpSens.getName() + "_pulled_" + y + ".dat"));

                    //System.err.println("Reading through file " + tmpSens.getName() + "_pulled_" + y + ".dat");

                    while(tmpSc.hasNext()) {

                        @SuppressWarnings("unused")
						String name = tmpSc.next();

                        //System.err.println("name = " + name);

                        if(tmpSc.hasNext()) {

                            @SuppressWarnings("unused")
							String capability = tmpSc.next();

                            //System.err.println(capability);

                            @SuppressWarnings("unused")
							String sameName = tmpSc.next();

                            //System.err.println(sameName);

                            int begin = tmpSc.nextInt();
                            @SuppressWarnings("unused")
							int end = tmpSc.nextInt();

                            //System.err.println(begin + " " + end);

                            if(begin > maxLength) {
                                maxLength = begin;
                            }

                            if(avgCounter.containsKey(begin)) {

                                Integer tmpInt = avgCounter.get(begin);

                                tmpInt += 1;

                                //System.err.println("Putting " + begin + " = " + tmpInt);

                                avgCounter.put(begin, tmpInt);
                            } else {

                                //System.err.println("Putting " + begin + " = " + 0);

                                avgCounter.put(begin, 0);
                            }
                        } else {

                            //System.err.println("breaking");

                            break;
                        }
                   }
                }

                // avgCounter now contains all the results. Ready to make a
                // result file.

                String outAvgName = tmpSens.getName() + "_average.dat";

                BufferedWriter outAvg = new BufferedWriter(
                		new FileWriter(expPrefix + "/" + outAvgName));

                //System.err.println("maxLength = " + maxLength);

                for(int u = 0; u < numSecs; u++) {

                    if(avgCounter.containsKey(u)) {

                        double result = ((double)avgCounter.get(u))/
                        ((double) numRuns);

                        //System.err.println(u + "\t" + result + "\n");

                        outAvg.write(u + "\t" + result + "\n");

                    } else {

                        //System.err.println(u + "\t" + 0 + "\n");

                        outAvg.write(u + "\t" + 0 + "\n");
                    }
                }

                outAvg.close();

                if(maxLength > 0) {
                    outGnu.write("\"" + outAvgName + "\" using 1:2 title \"" + 
                    		tmpSens.getName() + "\" with lines");

                    if(i < tmpSensors.size() - 1) {
                        outGnu.write(", ");
                    }
                }
            }

            outGnu.close();

        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
        }
    }

    /**
     * Generates plots for the test where real sensors are pulled (EiMM10).
     * 
     * @param scanners
     * @param expPrefix
     */
    
	public static void genStatsForMoreRealSensors(Scanner[] scanners,
			String[] filenames, String expPrefix) {
		
		Measure [] measure = new Measure[scanners.length]; 

		for(int i = 0; i < scanners.length; i++) {
			
			Scanner sc = scanners[i];

			// Read the current file.

			while(sc.hasNext()) {

	            long time = Long.valueOf(sc.next());
	            double batchTime = Double.valueOf(sc.next());
	            double batchSize = Double.valueOf(sc.next());
	            double evaluateBatch = Double.valueOf(sc.next());
	            double queuePulling = Double.valueOf(sc.next());
	            double stateEvaluation = Double.valueOf(sc.next());
	            double getTimeTupleSourcesToPull = Double.valueOf(sc.next());
	            double getTupleSourcesToPull = Double.valueOf(sc.next());
	

                System.err.println("Putting: " + time + " " + batchTime + " " +
                        batchSize + " " + evaluateBatch + " " + queuePulling + 
                        " " +
                        stateEvaluation + " " + getTupleSourcesToPull);

                if(measure[i] == null) {
                
                	measure[i] = new Measure(time, batchTime,
	                        batchSize, evaluateBatch, queuePulling,
	                        stateEvaluation, getTimeTupleSourcesToPull,
	                        getTupleSourcesToPull);
                } else {
                	
	                System.err.println("Adding: " + batchTime + " " +
	                        batchSize + " " + evaluateBatch + " " + 
	                        queuePulling + " " +
	                        stateEvaluation + " " + getTupleSourcesToPull);
	
	                measure[i].addLine(batchTime, batchSize,
	                        evaluateBatch, queuePulling,
	                        stateEvaluation, getTimeTupleSourcesToPull,
	                        getTupleSourcesToPull);
	            }
	        }
		}
		
		/**
		 * Each measure[i] contains the statistics of the runs. Write these
		 * to a result file. 
		 */
		
		BufferedWriter outResults = null;

		try {
			outResults = new BufferedWriter(new FileWriter(expPrefix + 
					"/results.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < filenames.length; i++) {
			try {
				outResults.write(measure[i].getLine() + "\t#" + filenames[i] + 
						"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		BufferedWriter outGnu = null;
		
		try {
			outGnu = new BufferedWriter(new FileWriter(expPrefix + 
					"/pulled_sensors.dem"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			outGnu.write("set term postscript eps enhanced\n"
			        + "set xlabel 'Number of sensors'\n"
			        + "set ylabel 'Time (milliseconds)'\n"
			        + "set output \"real_sensors_pulled.eps\"\n"
			        + "plot 'results.dat' using 5:20:21:22 with " +
			        		"errorbars notitle");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			outResults.close();
			outGnu.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a plot that shows the activity of the sensors together
	 * with the state machine. 
	 * 
	 * @param sensorFiles
	 * @param scanners
	 * @param smFile
	 * @param smScanner
	 * @throws IOException 
	 */
	
	public static void createGnuplotFromRun(String[] sensorFiles,
			Scanner[] scanners, String[] smFile, Scanner smScanner,
			String expPrefix, String fromDirectory) throws IOException {

		String[] resultFiles = new String[scanners.length]; 
		
		for(int i = 0; i < scanners.length; i++) {
			
			BufferedWriter outTemp = null;
			
			resultFiles[i] = new String(sensorFiles[i] + "_results.dat");
			
			outTemp = new BufferedWriter(new FileWriter(expPrefix + 
						"/" + resultFiles[i]));

			outTemp.write("# " + sensorFiles[i] + "\n");
			
			while(scanners[i].hasNext()) {
			
				String line = scanners[i].nextLine();
				
				System.err.println("line = " + line);
				
				String[] attrs = line.split("\t| +");
				
				/**
				 * Turn the ON readings to a value representative for this
				 * sensor. Divide timestamp on 1000 since it is in
				 * milliseconds.
				 */
				
				if(attrs[3].equals("true")) {
					outTemp.write(((Integer.parseInt(attrs[0])/1000)) + "\t" +
							(i + 1) + "\n");
				} else {
					System.err.println("attrs[3] = " + attrs[3]);
				}
			}
				
			outTemp.close();			
		}
		
		// Create gnuplot file.
		
		BufferedWriter outGnu = new BufferedWriter(new FileWriter(expPrefix + 
					"/resultFromRun.dem"));
		
		outGnu.write("# Results from directory " + fromDirectory + "\n");
		
		outGnu.write("set term postscript eps enhanced\n"
				+ "set grid\n"
				+ "set xtics 1\n"
				+ "set pointsize 2\n"
		        + "set xlabel 'Time'\n"
		        + "set yrange [0:" + (scanners.length + 0.5) + "]\n"
		        + "set ylabel 'Sensors'\n" +
		        		"set ytics (");
		
		for(int i = 0; i < sensorFiles.length; i++) {
			
			String sensorName = sensorFiles[i].replaceFirst("_0.dat", 
					"");
			
			sensorName = sensorName.replaceFirst("_", "\\\\_");
			
			outGnu.write("'" + sensorName + "' " + (i + 1));
			
			if(i < sensorFiles.length - 1) {
				outGnu.write(", ");
			}
		}
		
		outGnu.write(")\n" +
				"set output \"resultFromRun.eps\"\n");

		/** 
		 * Create arrows to show state evaluation. The arrows are created by
		 * using three values:
		 * 1. When the machine starts evaluating.
		 * 2. When the conditions are matched.
		 * 3. When the window is finished. 
		 */
		
		ArrayList<String> arrows = new ArrayList<String>(); 
		
		while(smScanner.hasNext()) {
			
			String[] tmpString = smScanner.nextLine().split("\t");
			
			System.err.println("Adding " + tmpString[0] + " to arrows");
			
			arrows.add(tmpString[0]);
		}
		
		System.err.println("arrows.size() = " + arrows.size());
		
		for(int i = 0; i < arrows.size() - 2; i += 3) {
			outGnu.write("set arrow " + (i + 1) + " from " + 
					(Integer.parseInt(arrows.get(i))/1000) + 
					",0.5 to " + 
					(Integer.parseInt(arrows.get(i + 1))/1000) + ",0.5\n");
			
			outGnu.write("set arrow " + ((2*i) + 1) + " from " + 
					(Integer.parseInt(arrows.get(i))/1000) + 
					",0.5 to " + 
					(Integer.parseInt(arrows.get(i + 2))/1000) + ",0.5\n");
			
			// Arrow head both ways.
			
			outGnu.write("set arrow " + (i + arrows.size()) + " to " + 
					(Integer.parseInt(arrows.get(i))/1000) + 
					",0.5 from " + 
					(Integer.parseInt(arrows.get(i + 2))/1000) + ",0.5\n");
		}
				
		outGnu.write("plot ");
		
		for(int i = 0; i < resultFiles.length; i++) {
			
			outGnu.write("'" + resultFiles[i] + "' using 1:2 with points pt 5" +
					" notitle");
			
			if(i < resultFiles.length - 1) {
				outGnu.write(", ");
			}			
		}

		outGnu.close();
	}
}
