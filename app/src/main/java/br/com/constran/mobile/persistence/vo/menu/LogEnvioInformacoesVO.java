package br.com.constran.mobile.persistence.vo.menu;

import br.com.constran.mobile.persistence.vo.AbstractVO;

/**
 * Created by mateus_vitali on 04/11/2014.
 */
public class LogEnvioInformacoesVO extends AbstractVO {

    private static final long serialVersionUID = -1L;

    private Integer id;
    private String arquivo;
    private String data;
    private String obra;
    private String dataHoraEnvio;

    public LogEnvioInformacoesVO(Integer id, String arquivo, String data, String obra, String dataHoraEnvio) {
        this.id = id;
        this.arquivo = arquivo;
        this.data = data;
        this.obra = obra;
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public LogEnvioInformacoesVO(String arquivo, String data, String obra, String dataHoraEnvio) {
        this.arquivo = arquivo;
        this.data = data;
        this.obra = obra;
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public LogEnvioInformacoesVO(String data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    public String getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(String dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }
}
