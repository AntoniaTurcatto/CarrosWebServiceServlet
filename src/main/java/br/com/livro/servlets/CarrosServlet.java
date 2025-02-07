package br.com.livro.servlets;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import br.com.livro.domain.Response;
//import br.com.livro.domain.ListaCarros;
import br.com.livro.util.RegexUtil;
//import br.com.livro.util.JAXBUtil;
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
		
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		if(id != null) {
			//informou o id
			Carro carro = carroService.getCarro(id);
			if(carro != null) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(carro);
				ServletUtil.writeJSON(resp, json);
			} else {
				resp.sendError(404, "Carro não encontrado");
			}
		} else {
			//lista de carros
			List<Carro> carros = carroService.getCarros();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(carros);
			ServletUtil.writeJSON(resp, json);
		}
		//gera o XML
//		try {
//			//System.out.println("indo do get para o JAXBUtil");
//			//String xml = JAXBUtil.toXML(lista);
//			//escreve o xml na response Http
//			//System.out.println("indo do get para o ServletUtil");
//			//ServletUtil.writeXML(resp, xml);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Carro carro = getCarroFromRequest(req);
		//salva o carro
		carroService.save(carro);
		//escreve o Json do carro salvo
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ServletUtil.writeJSON(resp, gson.toJson(carro)); 
	} 
	
	
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//neste caso,é necessário enviar no URL o id
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		System.out.println(id);
		Response r;
		if(id != null) {
			if(carroService.delete(id)) {
				r = Response.ok("Carro excluido com sucesso");
			} else {
				r = Response.error("Carro não encontrado ou erro ao excluir");
			}
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			ServletUtil.writeJSON(resp, gson.toJson(r));
			
		} else {
			//url inválida
			resp.sendError(404, "URL inválida");
		}
	}

	private Carro getCarroFromRequest(HttpServletRequest request) {
		//String tipo, String nome, String desc, String urlFoto, String urlVideo, String latitude,
		//String longitude
		String id = request.getParameter("id");
		Carro c;
		if(id != null) {
			//se informou o id, busca no banco
			c = carroService.getCarro(Long.parseLong(id));
		} else {
			c = new Carro();
		}
		c.setTipo(request.getParameter("tipo"));
		c.setNome(request.getParameter("nome"));
		c.setDesc(request.getParameter("descricao"));
		c.setUrlFoto(request.getParameter("url_foto"));
		c.setUrlVideo(request.getParameter("url_video"));		
		c.setLatitude(request.getParameter("latitude"));
		c.setLongitude(request.getParameter("longitude"));
		return c;
	}
	
	

}
