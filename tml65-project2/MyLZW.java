/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *************************************************************************/
import java.util.*;

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int W = 9;         // codeword width to start - max is 16
    private static int L = (int)Math.pow(2, W);          // number of codewords = 2^W

    public static void compress(char mode) 
    { 
    	BinaryStdOut.write(mode);
    	
      /*  int generatedBits = 0, totalBits = 0;		//for monitor mode that I couldn't get to work
        float oldRatio = 1, currRatio = 0;
        boolean monitor = false;*/
    	
        //System.err.println("L = "+L+" W = "+W);
		String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        
    	switch(mode)
    	{
    	case 'n':		//do nothing mode

    		//System.err.println("Mode is do nothing mode");
	        while (input.length() > 0)
	        {
	            String s = st.longestPrefixOf(input);  // Find max prefix match s.
	            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
	            int t = s.length();
	            
	            if (t < input.length())    // Add s to symbol table.
	            {
	            	if(code >= L && W < 16)	//W is a max of 16 
		            {
		            	W = W + 1;		// update variables l and w
		            	L = (int)Math.pow(2,W);
		            	//System.err.println("We are in\n W = "+W+"\nL = "+L);
		            }
	            	if(code < L)		//add to symbol table
	            		st.put(input.substring(0, t + 1), code++);
	            }
	            input = input.substring(t);            // Scan past s in input.
	        }
	        BinaryStdOut.write(R, W);
	        BinaryStdOut.close();
    		
    	break;
    	
    	
    	case 'r': 		//reset mode
    		
    		//System.err.println("Mode is reset mode");
    		
    		while (input.length() > 0)
	        {
	            String s = st.longestPrefixOf(input);  // Find max prefix match s.
	            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
	            int t = s.length();
	            
	           // System.err.println("string = "+s+" -----  code = "+code);
	            
	            if (t < input.length())    // Add s to symbol table.
	            {
	            	if(code >= L && W == 16) //we need to reset
	            	{
	            		W = 9; 						//reset W and L
	            		L = (int) Math.pow(2, W);
	            		st = new TST<Integer>();		//create new TST
		                for (int i = 0; i < R; i++)
		                    st.put("" + (char) i, i);
		                code = R+1;  // R is codeword for EOF
	            	}
	            	
	            	if(code >= L && W < 16)	//W is a max of 16 
		            {
		            	W = W + 1;		// update variables l and w
		            	L = (int)Math.pow(2,W);
		            }
	            	
	            	if(code < L)		//add to symbol table
	            		st.put(input.substring(0, t + 1), code++);
	            }
	            input = input.substring(t);            // Scan past s in input.
	        }
	        BinaryStdOut.write(R, W);
	        BinaryStdOut.close();
    		
    	break;
    	
 
    	case 'm':		//monitor mode
    		
    		System.err.println("Mode is monitor mode -- code is commented out because it did not");
    		System.err.println("function correctly. It ran without error but compress was not correct for some");
    		System.err.println("and expand was correct for some but not all");
    		 /*while (input.length() > 0)
 	        {
    			 
 	            String s = st.longestPrefixOf(input);  // Find max prefix match s.
 	            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
 	            int t = s.length();
	          //  System.err.println("gen = "+generatedBits+"  totalbits = "+totalBits);

 	            generatedBits += t*8; 
 	            totalBits += W; 	
 	          // System.err.println("In bits = "+generatedBits+"      bits where we +W = "+totalBits);
	           // System.err.println("gen = "+generatedBits+"  totalbits = "+totalBits);

 	            
 	            if (t < input.length())    // Add s to symbol table.
 	            {
 	            	//System.err.println("gen = "+generatedBits+"  totalbits = "+totalBits);
 	            	currRatio = generatedBits / (float)totalBits; //create current ratio of compression
 	            	//System.err.println("currRatio = "+currRatio);
 	            	
 	            	if(!monitor && (W == 16 && oldRatio/currRatio <= 1.1)) 	//we are currently starting the monitoring
 	            	{
 	            		oldRatio = currRatio;
 	            		monitor = true;
 	            		System.err.println("Monitoring Started, OldRatio = "+oldRatio);
 	            		System.err.println("ratio of ratios = "+oldRatio/currRatio);
 	            	}
 	            	
 	            	else if((oldRatio)/(currRatio) > 1.1 && W == 16)
 	            	{
 	            		//System.err.println("ratio of ratios = "+oldRatio/currRatio);
 	            		if(code >= L && W == 16) //we need to reset
 		            	{
 		            		W = 9; 
 		            		L = (int) Math.pow(2, W);
 		            		st = new TST<Integer>();
 			                for (int i = 0; i < R; i++)
 			                    st.put("" + (char) i, i);
 			                code = R+1;  // R is codeword for EOF
 		            	}
 	            		monitor = false;
 	            		//System.err.println("Monitoring stopped due to exceeded compression ratio "+(oldRatio/currRatio));
 	            	}
 	            	if(code >= L && W < 16)	//W is a max of 16 
 		            {
 		            	W = W + 1;		// update variables l and w
 		            	L = (int)Math.pow(2,W);
 		            	System.err.println("W = "+W);
 		            	//System.err.println("We are in\n W = "+W+"\nL = "+L);
 		            }
 	            	if(code < L)		//add to symbol table
 	            		st.put(input.substring(0, t + 1), code++);
 	            }
 	            input = input.substring(t);            // Scan past s in input.
 	        }
 	        BinaryStdOut.write(R, W);
 	        BinaryStdOut.close();
    		
    	break;*/
    		
    	}
    } 


    public static void expand() 
    {
        ArrayList<String> st = new ArrayList<String>();
        int i; // next available codeword value

        char mode = BinaryStdIn.readChar();
        
       /* int generatedBits = 0, totalBits = 0;		//for monitor mode that i couldnt get working
        float oldRatio = 1, currRatio = 0;
        boolean monitor = false;*/
        
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st.add("" + (char) i);
        i++;
        st.add("");                        // (unused) lookahead for EOF
        i = R+1;

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st.get(codeword);

        while(true)
        {
        	/*generatedBits += val.length()*8;  // for monitor mode commented out since it didnt work correctly
        	totalBits += W;*/
        	        	
	        switch(mode)
	        {
	        case 'n':
	    		//System.err.println("Mode is do nothing mode");
	        		           
	        	break; //we dont want to do anything so just break and do whats below
	        	
	        case 'r':
	    		//System.err.println("Mode is reset mode");
	        	//System.err.println("String = "+st.get(codeword)+" ------ code = "+i);
	    		
	    		if(i >= L && W == 16) //we need to reset
            	{
	    			st = new ArrayList<String>(); 		//make new arraylist
		  	          
	  	          // initialize symbol table with all 1-character strings
	  	        	for (i = 0; i < R; i++)
	  	        		st.add("" + (char) i);
	  	        	i++;
	  	        	st.add("");                        // (unused) lookahead for EOF
	  	        	
            		W = 9; 							//reset W and L
            		L = (int) Math.pow(2, W);
	                i = R+1;  // R is codeword for EOF bypass the EOF marker
            	}
	    			        	
	        	break;
	        	
	        case 'm':
	    		System.err.println("Mode is monitor mode -- code is commented out because it did not");
	    		System.err.println("function correctly. It ran without error but compress was not correct for some");
	    		System.err.println("and expand was correct for some but not all");

	    		/*currRatio = generatedBits / totalBits;			//this is the code I had for monitor expand -- did not function correclty
	    		if(!monitor && (W == 1 && oldRatio/currRatio <= 1.1))
	    		{
	    			oldRatio = currRatio;
	    			monitor = true;
	    			System.err.println("Monitoring Started, OldRatio = "+oldRatio);
	    			System.err.println("ratio of ratios = "+oldRatio/currRatio);
	    		}
         	
	    		else if((oldRatio)/(currRatio) > 1.1 && W == 16)
	    		{
	         		System.err.println("ratio of ratios = "+oldRatio/currRatio);
	         		if(i >= L && W == 16) //we need to reset
		            	{
		            		W = 9; 
		            		L = (int) Math.pow(2, W);
		            		st = new ArrayList<String>();
			                for (i = 0; i < R; i++)
			                    st.add("" + (char) i);
			                i = R+1;  // R is codeword for EOF
		            	}
	         		monitor = false;
	         		System.err.println("Monitoring stopped due to exceeded compression ratio "+(oldRatio/currRatio));
	         	}*/
	    			
	        	break;
	        
	        }
	        
	        if(mode == 'n')
	        {
		        BinaryStdOut.write(val);
	  	        codeword = BinaryStdIn.readInt(W);
	  	        if (codeword == R) break;
	  	        String s;
	  	        if (i == codeword) s = val + val.charAt(0);   // special case hack
	  	        else  s = st.get(codeword);
	  	       // System.err.println("outside switch \ni = "+i+" L = "+L);
	  	        if (i < L) 
	  	        {
	  	        	st.add(val + s.charAt(0));
	  	        	//System.err.println("adding = "+st.get(codeword));
	  	        	i++;
	  	        }
	  	        val = s;
	  	        
	  	        if(i >= L && W < 16) //we want to update W and L variables
	  	        {
	  	        	//System.err.println("updating W and L");
	  	        	W = W + 1;
	  	     	    L = (int)Math.pow(2, W);
	  	        }
	        }
	        
	        else if(mode == 'r')
	        {
	        	BinaryStdOut.write(val);
	  	        codeword = BinaryStdIn.readInt(W);
	  	        if (codeword == R) break;
	  	        String s;
	  	        if (i == codeword) s = val + val.charAt(0);   // special case hack
	  	        else  s = st.get(codeword);
	  	        

	  	        if (i < L) 
	  	        {
	  	        	st.add(val + s.charAt(0));
	  	        	//System.err.println("adding = "+st.get(codeword));
	  	        	i++;
	  	        }
	  	        
	  	        val = s;
	  	        
	  	        if(i >= L && W < 16) //we want to update W and L variables
	  	        {
	  	        	//System.err.println("updating W and L");
	  	        	W = W + 1;
	  	     	    L = (int)Math.pow(2, W);
	  	        }
	        	
	        }
	        
	        else if(mode == 'm')		//for monitor mode 
	        {
	        	BinaryStdOut.write(val);
	  	        codeword = BinaryStdIn.readInt(W);
	  	        if (codeword == R) break;
	  	        String s;
	  	        if (i == codeword) s = val + val.charAt(0);   // special case hack
	  	        else  s = st.get(codeword);
	  	        

	  	        if (i < L) 
	  	        {
	  	        	st.add(val + s.charAt(0));
	  	        	//System.err.println("adding = "+st.get(codeword));
	  	        	i++;
	  	        }
	  	        
	  	        val = s;
	  	        
	  	        if(i >= L && W < 16) //we want to update W and L variables
	  	        {
	  	        	//System.err.println("updating W and L");
	  	        	W = W + 1;
	  	     	    L = (int)Math.pow(2, W);
	  	        }
	        	
	        }
	    }
        BinaryStdOut.close();
    }

//////////////////////////// MAIN ////////////////////////////////////////////////////////////////
    
    public static void main(String[] args)
    {
    	char mode;				// handles input cases for n, r and m
    	
        if(args[0].equals("-")) 
        {
        	mode = args[1].charAt(0);
        	if(mode == 'n' || mode == 'r' || mode == 'm')
        		compress(mode);		//pass in the mode to use
        	else throw new IllegalArgumentException("Invalid mode entered");
        }
        	
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
        
    }

}
