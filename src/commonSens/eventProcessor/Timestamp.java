package eventProcessor;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.5E4DD6CC-57F1-2E19-88B6-8B8BFB24D6BD]
// </editor-fold> 
public class Timestamp {

    long timestamp;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.F00BFE48-3DF6-EC70-D735-67D0A2178FDF]
    // </editor-fold> 
    public Timestamp (long timestamp) {

        this.timestamp = timestamp;
    }

    public Timestamp(Timestamp ts) {
        this.timestamp = new Long(ts.getTimestamp());
    }

    /**
     * Parses a timestamp from String and turns the timestamp into an
     * epoch timestamp with milliseconds granularity.
     *
     * The allowed formats are:
     *
     * - Numbers directly followed by 's', 'm' og 'h', e.g., 3m and 24h. Currently
     *   the user has to manually change e.g. 3m2s to 182s.
     * - Timestamps with infinitely many sub-seconds: 12:43:27.416392
     *
     * @param tmpTb
     */

    public Timestamp(String tmpTb) {

        if (tmpTb.matches("[0-9]+[s|m|h]")) {

            //System.err.println(tmpTb + " is set timestamp.");

            char denotion = tmpTb.charAt(tmpTb.length() - 1);

            long stamp = Integer.parseInt(tmpTb.substring(0, tmpTb.length() - 1));

            if (denotion == 's') {
                timestamp = stamp * 1000;
            } else if (denotion == 'm') {
                timestamp = stamp * 60000;
            } else /* denotion == 'h' */ {
                timestamp = stamp * 360000;
            }

        } else if (tmpTb.matches("^\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}.*")) {

            //System.err.println(tmpTb + " is ordinary timestamp.");

            timestamp = toLong(tmpTb);

        } else if (tmpTb.matches("[\\d]+")) {

            timestamp = Integer.parseInt(tmpTb) * 1000;

        } else if (tmpTb.equals("-1")) {

            timestamp = -1;

        } else {

            System.err.println("Unknown timestamp format.");
        }

        //System.err.println("timestamp = " + timestamp);
    }

    /**
     * Gives the timestamp in milliseconds, but the milliseconds are
     * rounded away.
     * 
     * @return
     */
    
    public long getTimestamp() {

    	return timestamp;
//    	return (timestamp/1000)*1000;
    }

//    public void setTimestamp(long time) {
//        this.timestamp = time;
//    }

    public String getName() {
        return String.valueOf(timestamp);
    }

    /**
     * Receives a string with a timestamp in this format:
     *
     * hr:min:sec.millisec
     *
     * @param tmpTb
     * @return the timestamp in milliseconds.
     */

    public static long toLong(String tmpTb) {

        String [] hms = tmpTb.split(":");

        long hourL = Long.parseLong(hms[0]);

        long minuteL = Long.parseLong(hms[1]);

        long hm = hourL * 3600000 + minuteL * 60000;

        String [] hmsLast = hms[2].split("\\.");

        hm += Long.parseLong(hmsLast[0]) * 1000;

        if (hmsLast.length == 2) {

            /**
             * There are also sub-seconds. Change to milliseconds.
             */

            long tmpL = Long.parseLong(hmsLast[1]);

            if(tmpL > 999) {

                tmpL /= 1000;
            }

            hm += tmpL;
        }

        /**
         * Dirty hack...
         */
        
        return round(hm);
    }
    
    public static long round(long ts) {
    	return (ts/1000)*1000;
    }
}

