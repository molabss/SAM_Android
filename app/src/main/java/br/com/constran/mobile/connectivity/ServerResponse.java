package br.com.constran.mobile.connectivity;


public class ServerResponse{
	
	public static final String E10 = "E10-INTERNAL_SERVER_ERROR-LOGICA DE PROGRAMACAO";
	public static final String E15 = "E15-INTERNAL_SERVER_ERROR-BANCO DE DADOS";
	public static final String E20 = "E20-BAD_REQUEST-PARAMETROS INCOMPLETOS";
	public static final String E25 = "E25-UNAUTHORIZED-ACAO NAO AUTORIZADA NAS ATUAIS CONDICOES";
	public static final String E30 = "E30-INTERNAL_SERVER_ERROR-CRIACAO DE DIRETORIO DE ARQUIVO";
	public static final String E35 = "E35-INTERNAL_SERVER_ERROR-CRIACAO DE ARQUIVO";
	public static final String E40 = "E40-INTERNAL_SERVER_ERROR-DESSERIALIZACAO DE DADOS JSON";
	public static final String E45 = "E45-NOT_FOUND-OS PARAMETROS INFORMADOS NAO SAO VALIDOS PARA PROCESSAR A REQUISICAO.";
	
	private Object entity;
	private String serverMessageUser;
	private String serverMessageConsole;
	private String troubleShootData;
	private String codeError;
	private Throwable rootCause;	
	
	public ServerResponse(){
	}
	
	public ServerResponse(Object entity, String serverMessageUser){
		this.entity = entity;
		this.serverMessageUser = serverMessageUser;
	}
	
	public ServerResponse(Object entity,String serverMessageUser,String serverMessageConsole){
		this.entity = entity;
		this.serverMessageUser = serverMessageUser;
		this.serverMessageConsole = serverMessageConsole;
	}
	
	public ServerResponse(String serverMessageUser,String serverMessageConsole){
		this.serverMessageUser = serverMessageUser;
		this.serverMessageConsole = serverMessageConsole;
	}
	
	public ServerResponse(String serverMessageUser,String serverMessageConsole,String codeError){
		this.serverMessageUser = serverMessageUser;
		this.serverMessageConsole = serverMessageConsole;
		this.codeError = codeError;
	}
	
	
	public ServerResponse(String serverMessageUser,String serverMessageConsole, Throwable rootCause){
		this.serverMessageUser = serverMessageUser;
		this.serverMessageConsole = serverMessageConsole;
		this.rootCause = rootCause;
	}
	
	public ServerResponse(String serverMessageUser,String serverMessageConsole, Throwable rootCause, String codeError){
		this.serverMessageUser = serverMessageUser;
		this.serverMessageConsole = serverMessageConsole;
		this.rootCause = rootCause;
	}
	
	
	public ServerResponse(String serverMessageUser,String serverMessageConsole, String troubleShootData, String codeError){
		this.serverMessageUser = serverMessageUser;
		this.serverMessageConsole = serverMessageConsole;
		this.troubleShootData = troubleShootData;
		this.codeError = codeError;
	}

	public String getServerMessageUser() {
		return serverMessageUser;
	}
	public void setServerMessageUser(String serverMessageUser) {
		this.serverMessageUser = serverMessageUser;
	}
	public String getServerMessageConsole() {
		return serverMessageConsole;
	}
	public void setServerMessageConsole(String serverMessageConsole) {
		this.serverMessageConsole = serverMessageConsole;
	}
	public String getTroubleShootData() {
		return troubleShootData;
	}
	public void setTroubleShootData(String troubleShootData) {
		this.troubleShootData = troubleShootData;
	}
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public String getCodeError() {
		return codeError;
	}

	public void setCodeError(String codeError) {
		this.codeError = codeError;
	}

	public Throwable getRootCause() {
		return rootCause;
	}

	public void setRootCause(Throwable rootCause) {
		this.rootCause = rootCause;
	}

}
