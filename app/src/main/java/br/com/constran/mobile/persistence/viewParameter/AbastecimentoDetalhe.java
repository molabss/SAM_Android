package br.com.constran.mobile.persistence.viewParameter;

/**
 * Created by moises_santana on 22/10/2015.
 */
public class AbastecimentoDetalhe {

    private String raeDate;
    private String prefixoEquipamento;
    private String descricaoPosto;
    private String combustivelLubrifDesc;
    private String nomeUsuario;
    private String frenteObraDesc;
    private String frenteObraAtivDesc;
    private String abastecimentoInicio;



    /*
       *
        select.append("select r.data,e.prefixo,p.descricao as descricao,c.descricao as descricao2,uo.nome"          );
        select.append(   ",f.descricao as descricao3,af.descricao as descricao4,a.horaInicio,a.horaTermino"         );
        select.append(   ",a.quilometragem,a.horimetro,jo.descricao as descricao5,a.quantidade,a.observacao"        );
        select.append(   ",e.descricao as descricao6 "                                                              );
        select.append("from abastecimentos a "                                                                      );
        select.append(    "inner join rae r "                                                                       );
        select.append(       "on r.idRae = a.rae "                                                                  );
        select.append(    "inner join combustiveisLubrificantes c "                                                 );
        select.append(       "on a.combustivel = c.idCombustivelLubrificante "                                      );
        select.append(    "inner join equipamentos e "                                                              );
        select.append(       "on e.idEquipamento = a.equipamento "                                                  );
        select.append(    "inner join postos p "                                                                    );
        select.append(       "on p.idPosto = r.posto "                                                              );
        select.append(    "inner join usuarios  ua "                                                                );
        select.append(       "on a.codAbastecedor = ua.codUsuario "                                                 );
        select.append(    "left join frentesObra f "                                                                );
        select.append(      "on f.idFrentesObra = af.frentesObra "                                                  );
        select.append(    "left join frentesObraAtividade af "                                                      );
        select.append(       "on af.atividade = a.atividade "                                                       );
        select.append(    "left join justificativasOperador  jo "                                                   );
        select.append(       "on jo. idJustificativaOperador  = a. justificativa "                                  );
        select.append("where  r.[idRae] = ? and a.[combustivel] = ? and a.[equipamento] = ? and a.[horaInicio] = ? ");
        select.append(   "order by e.[prefixo] asc, p.[descricao] asc, c.[descricao] asc"                           );
       *
     */

}
