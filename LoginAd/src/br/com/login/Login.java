package br.com.login;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingProvider;

import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPHeader;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.utils.Options;

import br.com.avianca.portal.Perfil;
import br.com.avianca.portal.UserCredentials;
import br.com.avianca.portal.WsPortalAvianca;
import br.com.avianca.portal.WsPortalAviancaLocator;
import br.com.avianca.portal.WsPortalAviancaSoap;
import br.com.avianca.portal.WsPortalAviancaSoapStub;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String use = request.getParameter("login");
		String pwd = request.getParameter("pwd");

		try {

			String s = login("anogueira","Senha#13");

			// getSecurity();
			System.out.println(s);

			//response.getWriter().append("Served at: ").append(s);

		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private static String login(String user, String pwd) throws SOAPException {
		Perfil perfil = null;

		try {

			//String ipAdress = InetAddress.getLocalHost().getHostAddress();

			WsPortalAvianca wsLocator = new WsPortalAviancaLocator();

			WsPortalAviancaSoap ws = wsLocator.getwsPortalAviancaSoap();
			
			//ws._getServiceClient().getOptions().setProperty(org.apache.axis2.Constants.Configuration.CHARACTER_SET_ENCODING,"iso-8859-1");
			
			

			SOAPHeaderElement headerElement = new SOAPHeaderElement("https://portal.avianca.com.br/","IpAddressHeader");
			//authentication.setEncodingStyle("utf-8");
			//headerElement.addNamespaceDeclaration("IpAddressHeader","https://portal.avianca.com.br/");
			//headerElement.setMustUnderstand(true);
			

			SOAPElement node = headerElement.addChildElement("IpAddress");
			node.addTextNode("192.168.1.132");

			//SOAPHeaderElement ip = new SOAPHeaderElement("IpAddress", "192.168.1.132");			
			
			/*((WsPortalAviancaSoapStub) ws).setHeader("https://portal.avianca.com.br/","IpAddressHeader","192.168.1.132");*/

			
			
			UserCredentials credentials = new UserCredentials(user, pwd);
			((WsPortalAviancaSoapStub) ws).setHeader(headerElement);

			ws.login(credentials);
			

			Perfil[] p = ws.getPerfil();

			perfil = p[0];

		} catch (ServiceException e) {
			System.out.println("ServiceException " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("RemoteException " + e.getMessage());
		}
		return perfil.getNomePerfil();

	}

	private static void getSecurity() {

		try {

			String ipAdress = InetAddress.getLocalHost().getHostAddress();
			WsPortalAvianca wsLocator = new WsPortalAviancaLocator();
			WsPortalAviancaSoap ws = wsLocator.getwsPortalAviancaSoap();

			SOAPHeaderElement authHeader = new SOAPHeaderElement("https://portal.avianca.com.br/", "AuthHeader");

			SOAPElement nodeauth = authHeader.addChildElement("Session");
			nodeauth.addTextNode("12345678");

			// SOAPHeaderElement ip = new
			// SOAPHeaderElement("Session","12345678");

			((WsPortalAviancaSoapStub) ws).setHeader(authHeader);

			SOAPHeaderElement ipHeader = new SOAPHeaderElement("https://portal.avianca.com.br/", "IpAddressHeader");

			SOAPElement node = ipHeader.addChildElement("IpAddress");
			node.addTextNode(ipAdress);

			// SOAPHeaderElement ip = new
			// SOAPHeaderElement("IpAddress",ipAdress);

			((WsPortalAviancaSoapStub) ws).setHeader(ipHeader);

			String s = ws.getSecurity();
			System.out.println("  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx   " + s);

		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException " + e.getMessage());
		} catch (ServiceException e) {
			System.out.println("ServiceException " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("RemoteException " + e.getMessage());
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private static String login2(String user, String pwd) throws SOAPException {
		Perfil perfil = null;

		try {

			WsPortalAvianca wsLocator = new WsPortalAviancaLocator();
			WsPortalAviancaSoap ws = wsLocator.getwsPortalAviancaSoap();			
			
			SOAPHeaderElement ipHeader = new SOAPHeaderElement("https://portal.avianca.com.br/", "IpAddressHeader");
/*
			SOAPElement node = ipHeader.addChildElement("IpAddress");
			node.addTextNode(ipAdress);*/
			
			
			UserCredentials credentials = new UserCredentials(user, pwd);
			((BindingProvider)ws).getBinding().getHandlerChain().add(new SOAPHeaderHandler("192.168.0.222"));

			ws.login(credentials);

			Perfil[] p = ws.getPerfil();

			perfil = p[0];

		} catch (ServiceException e) {
			System.out.println("ServiceException " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("RemoteException " + e.getMessage());
		}
		return perfil.getNomePerfil();

	}

}
