package br.com.livro.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import br.com.livro.domain.Response;
import br.com.livro.util.RegexUtil;
import br.com.livro.util.ServletUtil;

@WebServlet("/carros/*")
public class CarrosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private CarroService carroService = new CarroService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		
		if (id != null) {
			Carro carro = carroService.getCarro(id);
			if (carro != null) {
				String json = gson.toJson(carro);
				ServletUtil.writeJSON(resp, json);
			} else {
				resp.sendError(404,"Carro não encontrado");
			}
		} else {
			List<Carro> carros = carroService.getCarros();
			// ListaCarros lista = new ListaCarros();
			// lista.setCarros(carros);

			String json = gson.toJson(carros);
			// String json = JAXBUtil.toJSON(lista);
			ServletUtil.writeJSON(resp, json);
			
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Carro carro = getCarroFromRequest(req);
		carroService.save(carro);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(carro);
		ServletUtil.writeJSON(resp, json);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Response r;
		
		
		if(id != null ) {
			
			if(carroService.getCarro(id) == null) {
				r = Response.Error("Carro não encontrado");
			} else {
				carroService.delete(id);
				 r = Response.Ok("Carro excluído com sucesso");
			}
						
			String json = gson.toJson(r);
			ServletUtil.writeJSON(resp, json);
		} else {
			resp.sendError(404, "URL inválida");
		}
	}
	
	private Carro getCarroFromRequest(HttpServletRequest request) {
		Carro c = new Carro();
		
		String id = request.getParameter("id");
		
		if(id != null) {
			c = carroService.getCarro(Long.parseLong(id));
		}
		
		c.setNome(request.getParameter("nome"));
		c.setDesc(request.getParameter("descricao"));
		c.setUrlFoto(request.getParameter("url_foto"));
		c.setUrlVideo(request.getParameter("url_video"));
		c.setLatitude(request.getParameter("latitude"));
		c.setLongitude(request.getParameter("longitude"));
		c.setTipo(request.getParameter("tipo"));
		return c;
	}
}
