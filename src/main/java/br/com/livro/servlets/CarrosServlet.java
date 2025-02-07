package br.com.livro.servlets;

import java.io.IOException;
import java.util.List;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import br.com.livro.domain.ListaCarros;
import br.com.livro.util.JAXBUtil;
import br.com.livro.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/carros/*")
public class CarrosServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private CarroService carroService = new CarroService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Carro> carros = carroService.getCarros();
		ListaCarros lista = new ListaCarros();
		lista.setCarros(carros);
		//gera o XML
		try {
			System.out.println("indo do get para o JAXBUtil");
			String xml = JAXBUtil.toXML(lista);
			//escreve o xml na response Http
			System.out.println("indo do get para o ServletUtil");
			ServletUtil.writeXML(resp, xml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
