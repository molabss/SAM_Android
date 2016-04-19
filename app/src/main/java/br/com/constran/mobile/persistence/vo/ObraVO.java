package br.com.constran.mobile.persistence.vo;

public class ObraVO extends AbstractVO {

    private static final long serialVersionUID = -8647616957533842885L;

    private String exibirHorimetro;//S ou N
    private String horimetroObrigatorio;//S ou N
    private String usaQRCode;//S ou N
    private String usaQRCodePessoal;//S ou N
    private String usaOrigemDestino;//S ou N
    private String usaPlanServico;//S ou N

    public ObraVO(String descricao) {
        super(descricao);
    }

    public ObraVO(Integer id, String descricao) {
        super(id, descricao);
    }

    public ObraVO(Integer id, String descricao, String exibirHorimetro, String horimetroObrigatorio) {
        this(id, descricao);
        this.exibirHorimetro = exibirHorimetro;
        this.horimetroObrigatorio = horimetroObrigatorio;
    }

    public ObraVO(Integer id, String descricao, String exibirHorimetro, String horimetroObrigatorio,
                  String usaQRCode, String usaOrigemDestino) {
        this(id, descricao, exibirHorimetro, horimetroObrigatorio);
        this.usaQRCode = usaQRCode;
        this.usaOrigemDestino = usaOrigemDestino;
    }

    public ObraVO(Integer id) {
        super(id);
    }

    public String getExibirHorimetro() {
        return exibirHorimetro;
    }

    public void setExibirHorimetro(String exibirHorimetro) {
        this.exibirHorimetro = exibirHorimetro;
    }

    public String getHorimetroObrigatorio() {
        return horimetroObrigatorio;
    }

    public void setHorimetroObrigatorio(String horimetroObrigatorio) {
        this.horimetroObrigatorio = horimetroObrigatorio;
    }

    public String getUsaQRCode() {
        //return "S";
        return usaQRCode;
    }

    public void setUsaQRCode(String usaQRCode) {
        this.usaQRCode = usaQRCode;
    }

    public String getUsaQRCodePessoal() {
        return usaQRCodePessoal;
    }

    public void setUsaQRCodePessoal(String usaQRCodePessoal) {
        this.usaQRCodePessoal = usaQRCodePessoal;
    }

    public String getUsaOrigemDestino() {
        return usaOrigemDestino;
    }

    public void setUsaOrigemDestino(String usaOrigemDestino) {
        this.usaOrigemDestino = usaOrigemDestino;
    }

    @Override
    public String toString() {
        return getId().toString() + " - " + getDescricao();
    }

    public String getUsaPlanServico() {
        if(usaPlanServico == null)setUsaPlanServico("N");

        return usaPlanServico;
    }

    public void setUsaPlanServico(String usaPlanServico) {
        this.usaPlanServico = usaPlanServico;
    }
}
