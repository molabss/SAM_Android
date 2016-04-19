package br.com.constran.mobile.persistence.vo.rae.abs;

import java.io.Serializable;
import java.util.List;

/**
 * Criado em 12/05/14
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class SaldoPostoVO implements Serializable {

    private static final long serialVersionUID = -195202905562473720L;

    private Integer idObra;

    public SaldoPostoVO(Integer idObra, List<AbastecimentoPostoVO> saldos) {
        this.idObra = idObra;
        this.saldos = saldos;
    }

    public Integer getIdObra() {
        return idObra;
    }

    public void setIdObra(Integer idObra) {
        this.idObra = idObra;
    }

    private List<AbastecimentoPostoVO> saldos;

    public SaldoPostoVO(List<AbastecimentoPostoVO> abastecimentoPostoVOs) {
        this.saldos = abastecimentoPostoVOs;
    }

    public List<AbastecimentoPostoVO> getSaldos() {
        return saldos;
    }

    public void setSaldos(List<AbastecimentoPostoVO> saldos) {
        this.saldos = saldos;
    }
}
