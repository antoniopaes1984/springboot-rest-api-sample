package servletLogin;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOAhaRepository;
import dao.DAOAnamneseRepository;
import dao.DAOAvaliacaoRepository;
import dao.DAOCardiovascularRepository;
import dao.DAOCircunferenciaRepository;
import dao.DAOClienteRepository;
import dao.DAOComposicaoRepository;
import dao.DAONeuromuscularRepository;
import dao.DAOParqRepository;
import dao.DAOPosturalRepository;
import dao.DAOResultadoRepository;
import dao.DAOSomatotipoRepository;
import dao.DAOTMB;
import dao.DaoAvaliadorRepository;
import model.ModelAha;
import model.ModelAnamnese;
import model.ModelAvaliacao;
import model.ModelAvaliador;
import model.ModelCliente;
import model.ModelMedidas;
import model.ModelParq;
import model.ModelPostural;
import model.ModelSomatotipo;
import model.ModelTMB;
import regras.RegrasAvaliacao;

/**
 * Servlet implementation class ServletAvaliacao
 */
@WebServlet("/ServletAvaliacao")
public class ServletAvaliacao extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAOAvaliacaoRepository daoAvaliacao = new DAOAvaliacaoRepository();
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletAvaliacao() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		String acao = request.getParameter("acao");
		String codCliente = request.getParameter("codCliente");
		String codUsuario = request.getParameter("codusuario");
		if(acao.equals("novaavaliacao")) {
			DaoAvaliadorRepository daoAvaliador = new DaoAvaliadorRepository();
			ModelAvaliador avaliador = new ModelAvaliador();
			avaliador = daoAvaliador.pesquisaAvaliador(Integer.parseInt(codUsuario));
			
			ModelAvaliacao avaliacao = new ModelAvaliacao();
			//Captura a data de hoje do sistema
			RegrasAvaliacao regrasAvaliacao = new RegrasAvaliacao();
			String dataSistema = regrasAvaliacao.horaSistema();
			String dataReavaliacao = regrasAvaliacao.dataReavaliacao(dataSistema, avaliador.getDiasParaReavalia��o());
			ModelCliente cliente = new ModelCliente();
			DAOClienteRepository daoCliente = new DAOClienteRepository();
			cliente = daoCliente.pesquisaCliente(Integer.parseInt(codCliente));
			int idade = regrasAvaliacao.calculaIdade(cliente.getDataNascimento(), "yyyy-MM-dd");
			
			//Envia os dados da avalia��o para o banco
			avaliacao.setCodCliente((cliente.getCodCliente()));
			avaliacao.setCodUsuario(Integer.parseInt(codUsuario));
			avaliacao.setDataAvaliacao(dataSistema);
			avaliacao.setDataReavaliacao(dataReavaliacao);
			avaliacao.setIdadeCliente(idade);
			avaliacao.setNomeCliente(cliente.getNome());
			daoAvaliacao.novaAvaliacao(avaliacao);
			
			//Retorna os dados da avalia��o salva no banco
			ModelAvaliacao avaliacaoSalvaNoBanco = new ModelAvaliacao();
			avaliacaoSalvaNoBanco = daoAvaliacao.pesquisaAvaliacao(avaliacao.getCodCliente(), avaliacao.getDataAvaliacao());
			
			//Envia os dados da anamnese para o banco
			DAOAnamneseRepository daoAnamnese = new DAOAnamneseRepository();
			ModelAnamnese anamnese = new ModelAnamnese(avaliacaoSalvaNoBanco.getCodAvaliacao(),"N�o informada","N�o informada", "N�o informado", "N�o informado", "N�o informada", "N�o informado", "N�o informado", "N�o informada","N�o informada", "Sem classifica��o", "N�o informada", "Sem classifica��o", "N�o informada", 0, 0, "N�o informada", "N�o informada", 0, "Sem classifica��o", 0, "Sem classifica��o", 0, "Sem classifica��o", 0, "Sem classifica��o", 0, "Sem classifica��o", 0, "Sem classifica��o");
			daoAnamnese.inserirAnamneseBanco(anamnese);
			
			//Envia os dados do parq para o banco
			DAOParqRepository daoParq = new DAOParqRepository();
			ModelParq parq = new ModelParq(avaliacaoSalvaNoBanco.getCodAvaliacao(), "N�o", "N�o", "N�o", "N�o", "N�o", "N�o", "N�o", "N�o necessita parecer m�dico");
			daoParq.inserirParqBanco(parq);
			
			//Envia os dados do aha para o banco
			DAOAhaRepository daoAha = new DAOAhaRepository();
			ModelAha aha = new ModelAha(avaliacaoSalvaNoBanco.getCodAvaliacao(),false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,"N�o � necess�rio acompanhamento m�dico");
			daoAha.insereAhaBanco(aha);
			
			//Envia os dados do postural para o banco
			DAOPosturalRepository daoPostural = new DAOPosturalRepository();
			ModelPostural postural = new ModelPostural(avaliacaoSalvaNoBanco.getCodAvaliacao(), false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, 0, 0, 0, 0, 0, "", "");
			daoPostural.inserirPosturalBanco(postural);
			
			//Envia os dados das medidas de composi��o para o banco
			DAOComposicaoRepository daoComposicao = new DAOComposicaoRepository();
			daoComposicao.inserirMedidasBancoDadosBasico(avaliacaoSalvaNoBanco.getCodAvaliacao());
			
			DAOResultadoRepository daoResultado = new DAOResultadoRepository();
			daoResultado.insereDaoResultadoBasico(avaliacaoSalvaNoBanco.getCodAvaliacao());
			
			DAOCircunferenciaRepository daoCircunferencia = new DAOCircunferenciaRepository();
			daoCircunferencia.inserirCircunferenciaBanco(avaliacaoSalvaNoBanco.getCodAvaliacao());
			
			DAOTMB daoTMB = new DAOTMB();
			daoTMB.inserirTmb(avaliacaoSalvaNoBanco.getCodAvaliacao());
			
			DAOSomatotipoRepository daoSomatotipo = new DAOSomatotipoRepository();
			daoSomatotipo.inserirSomatotipo(avaliacaoSalvaNoBanco.getCodAvaliacao());
			
			DAOCardiovascularRepository daoCardio = new DAOCardiovascularRepository();
			daoCardio.inserirTesteCardioBasico(avaliacaoSalvaNoBanco.getCodAvaliacao());
			
			DAONeuromuscularRepository daoNeuro = new DAONeuromuscularRepository();
			daoNeuro.inserirAvaliacaoNeuroMuscularBasica(avaliacaoSalvaNoBanco.getCodAvaliacao());
			
				RequestDispatcher redireciona = request.getRequestDispatcher("/principal/principal.jsp");
				request.setAttribute("codAvaliacao", avaliacaoSalvaNoBanco.getCodAvaliacao());
				request.setAttribute("avaliacao", avaliacaoSalvaNoBanco);
				redireciona.forward(request, response);
		}else if(acao.equals("editar")) {
			String codavaliacao = request.getParameter("codavaliacao");
			DAOAvaliacaoRepository daoAvaliacao = new DAOAvaliacaoRepository();
			ModelAvaliacao avaliacao = new ModelAvaliacao();
			avaliacao = daoAvaliacao.pesquisaAvaliacaoPeloCodAvaliacao(Integer.parseInt(codavaliacao));
			RequestDispatcher redireciona = request.getRequestDispatcher("/principal/principal.jsp");
			request.setAttribute("codAvaliacao", avaliacao.getCodAvaliacao());
			request.setAttribute("avaliacao", avaliacao);
			redireciona.forward(request, response);
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Entrou na editar");
		String codAvaliacao = request.getParameter("txtCodAvaliacao");
		System.out.println("Cod de avaliacao para editar anamnese: "+codAvaliacao);
	}

}
