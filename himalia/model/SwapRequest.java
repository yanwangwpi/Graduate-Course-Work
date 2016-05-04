/*package himalia.model;

import broker.util.*;

import java.util.ArrayList;
import java.util.StringTokenizer;


*//**
 * 
 * @author susanqin
 *
 *//*
*//**
 * Insufficient error checking on parameters.
 * 
 *//*
public class SwapRequest {
	public final String  requestor_id;
	public       String  acceptor_id;   *//** initially '*' because requestor doesn't know who may accept *//*
	public final String  offerTypes[];
	public final String  offerWords[];
	public final String  requestTypes[];
	public final String requestWords[];
	public String Status;
	public final int wordCount;

	*//**
	 * Assumes all arguments are valid (could be cleaned up).
	 * @param id
	 * @param n
	 * @param offerTypes
	 * @param offerWords
	 * @param requestTypes
	 * @param requestWords
	 *//*
	public SwapRequest(String requestor_id, String acceptor_id, int n, String[] offerTypes, String[] offerWords,String[] requestTypes, String[] requestWords) {
		this.requestor_id = requestor_id;
		this.acceptor_id = acceptor_id;
		this.wordCount = n;
		this.offerTypes = offerTypes;
		this.offerWords = offerWords;
		this.requestTypes = requestTypes;
		this.requestWords = requestWords;
	}
	
	public final static SwapRequest extractSwap (StringTokenizer st) {
		String request_id = st.nextToken();
		String accept_id = st.nextToken();
		int n = Integer.valueOf(st.nextToken());
		
		String offerTypes[] = new String[n];
		String offerWords[] = new String[n];
		String requestTypes[] = new String[n];
		String requestWords[]=  new String[n] ;
		
		for (int i = 0; i < n; i++) {
			offerTypes[i] = st.nextToken();
		}
		for (int i = 0; i < n; i++) {
			offerWords[i]=(st.nextToken());
		}
		for (int i = 0; i < n; i++) {
			requestTypes[i] = st.nextToken();
		}
		for (int i = 0; i < n; i++) {
			requestWords[i]=st.nextToken();
		}
		
		return new SwapRequest (request_id, accept_id, n, offerTypes, offerWords, requestTypes, requestWords);
	}
	
	*//** Return encoding of swap object including requestor/acceptor ids *//*
	public String flatten() {
		StringBuffer sb = new StringBuffer();
		sb.append(requestor_id); sb.append(IProtocol.separator);
		sb.append(acceptor_id); sb.append(IProtocol.separator);
		sb.append(wordCount); sb.append (IProtocol.separator);
		for (int i = 0; i < wordCount; i++) {
			sb.append(offerTypes[i]); sb.append(IProtocol.separator);
		}
		for (int i = 0; i < wordCount; i++) {
			sb.append(offerWords[i]); sb.append(IProtocol.separator);
		}
		for (int i = 0; i < wordCount; i++) {
			sb.append(requestTypes[i]); sb.append(IProtocol.separator);
		}
		for (int i = 0; i < wordCount; i++) {
			sb.append(requestWords[i]); sb.append(IProtocol.separator);
		}
		
		return sb.toString();
	} 
	public String getStatus(){
		return Status;
	}
}
*/