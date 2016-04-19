package br.com.constran.mobile.persistence.vo.rae.abs;

import br.com.constran.mobile.persistence.vo.AbstractVO;
import br.com.constran.mobile.persistence.vo.imp.AtividadeVO;
import br.com.constran.mobile.persistence.vo.imp.EquipamentoVO;
import br.com.constran.mobile.persistence.vo.imp.UsuarioVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

public class AbastecimentoVO extends AbstractVO {
    private static final long serialVersionUID = 5330043445661569242L;
    //PK
    private Integer idRae;
    private EquipamentoVO equipamento;
    private CombustivelLubrificanteVO combustivelLubrificante;
    private String horaInicio;

    //FK
    private UsuarioVO abastecedor;
    private UsuarioVO operador;
    private Integer idObra;
    private AtividadeVO atividade;
    private JustificativaOperadorVO justificativa;

    private String horaTermino;
    private String qtd;
    private String tipo;

    private String horimetro;
    private String quilometragem;
    private String observacao;
    private String observacaoJustificativa;

    private Collection<LubrificacaoDetalheVO> lubrificacaoDetalhes;

    public AbastecimentoVO() {

    }

    public AbastecimentoVO(String id, String token) {

        if (id != null) {

            StringTokenizer st = new StringTokenizer(id, token);

            this.idRae = Integer.parseInt(st.nextToken());
            this.equipamento = new EquipamentoVO(Integer.parseInt(st.nextToken()));
            this.combustivelLubrificante = new CombustivelLubrificanteVO(Integer.parseInt(st.nextToken()));
            this.horaInicio = st.nextToken();
        }


        this.strId = id;
        this.lubrificacaoDetalhes = new ArrayList<LubrificacaoDetalheVO>();

    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public EquipamentoVO getEquipamento() {
        return equipamento;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public CombustivelLubrificanteVO getCombustivelLubrificante() {
        return combustivelLubrificante;
    }

    public UsuarioVO getAbastecedor() {
        return abastecedor;
    }

    public UsuarioVO getOperador() {
        return operador;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public String getQtd() {
        return qtd;
    }

    public String getTipo() {
        return tipo;
    }

    public String getHorimetro() {
        return horimetro;
    }

    public String getQuilometragem() {
        return quilometragem;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setEquipamento(EquipamentoVO equipamento) {
        this.equipamento = equipamento;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setCombustivelLubrificante(
            CombustivelLubrificanteVO combustivelLubrificante) {
        this.combustivelLubrificante = combustivelLubrificante;
    }

    public void setAbastecedor(UsuarioVO abastecedor) {
        this.abastecedor = abastecedor;
    }

    public void setOperador(UsuarioVO operador) {
        this.operador = operador;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setHorimetro(String horimetro) {
        this.horimetro = horimetro;
    }

    public JustificativaOperadorVO getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(JustificativaOperadorVO justificativa) {
        this.justificativa = justificativa;
    }

    public String getObservacaoJustificativa() {
        return observacaoJustificativa;
    }

    public void setObservacaoJustificativa(String observacaoJustificativa) {
        this.observacaoJustificativa = observacaoJustificativa;
    }

    public void setQuilometragem(String quilometragem) {
        this.quilometragem = quilometragem;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Collection<LubrificacaoDetalheVO> getLubrificacaoDetalhes() {
        return lubrificacaoDetalhes;
    }

    public void setLubrificacaoDetalhes(
            Collection<LubrificacaoDetalheVO> lubrificacaoDetalhes) {
        this.lubrificacaoDetalhes = lubrificacaoDetalhes;
    }

    public AtividadeVO getAtividade() {
        return atividade;
    }

    public void setAtividade(AtividadeVO atividade) {
        this.atividade = atividade;
    }

    public Integer getIdRae() {
        return idRae;
    }

    public Integer getIdObra() {
        return idObra;
    }

    public void setIdObra(Integer idObra) {
        this.idObra = idObra;
    }

    public void setIdRae(Integer idRae) {
        this.idRae = idRae;
    }


}
