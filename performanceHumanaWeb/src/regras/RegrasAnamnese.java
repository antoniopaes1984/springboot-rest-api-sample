package regras;

public class RegrasAnamnese {
	
	public String retornaPressaoSistolica (String pressaoArterial) throws Exception{
		String pressaoArterialRecebida = pressaoArterial;
		String[] s = pressaoArterialRecebida.split("x");
		 String press�oSistolica = s[0];
		 return press�oSistolica;
	}
	
	public String retornaPressaoDiastolica(String pressaoArterial) throws Exception{
		String pressaoArterialRecebida = pressaoArterial;
		String[] s = pressaoArterialRecebida.split("x");
		 String press�oSistolica = s[1];
		 return press�oSistolica;
	}
	
	public boolean verificaSePressaoArterialFoiSelecionada(String pressaoArterial)throws Exception{
		if(pressaoArterial.equals("N�o informada")) {
			return false;
		}else {
			return true;
		}
	}

	
}
