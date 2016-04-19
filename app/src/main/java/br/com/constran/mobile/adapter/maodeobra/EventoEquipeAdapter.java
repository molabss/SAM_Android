package br.com.constran.mobile.adapter.maodeobra;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.enums.Operacao;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.EventoEquipeVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.TipoApontamento;

import java.util.List;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class EventoEquipeAdapter extends AbstractAdapter<EventoEquipeVO> {

    private boolean readOnly;
    private boolean apontamentoAutomatico;

    public EventoEquipeAdapter(Context context, List<EventoEquipeVO> list, boolean readOnly) {
        super(context, R.layout.evento_grid_item, list);
        this.readOnly = readOnly;
    }

    public EventoEquipeAdapter(Context context, List<EventoEquipeVO> list, TipoApontamento apontamento) {
        this(context, list, true);
        this.apontamentoAutomatico = TipoApontamento.AUTOMATICO.equals(apontamento);
    }

    @Override
    protected void preencherItensGrid(View view, int i) {
        TextView horarioText = (TextView) view.findViewById(R.id.horarioText);
        TextView eventoText = (TextView) view.findViewById(R.id.eventoText);
        ImageView relogioImage = (ImageView) view.findViewById(R.id.relogioImage);
        ImageView excluirImage = (ImageView) view.findViewById(R.id.excluirImage);
        ImageView editarImage = (ImageView) view.findViewById(R.id.editarImage);

        final EventoEquipeVO eventoEquipe = list.get(i);

        String dataIni = eventoEquipe.getHoraIni();
        String dataFim = eventoEquipe.getHoraFim();
        String horario = dataIni;
        String descricao = eventoEquipe.getParalisacao() != null ? eventoEquipe.getParalisacao().getDescricao() : "";

        if (dataFim != null && !dataFim.isEmpty()) {
            horario = dataIni + " a " + dataFim;
            relogioImage.setVisibility(View.INVISIBLE);
        }

        horarioText.setText(horario);
        eventoText.setText(descricao);

        if (readOnly || apontamentoAutomatico) {
            excluirImage.setVisibility(View.GONE);
        }

        if (apontamentoAutomatico) {
            editarImage.setVisibility(View.GONE);
        }

        editarImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EDICAO;
                return false;
            }
        });

        excluirImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EXCLUSAO;
                return false;
            }
        });
    }
}

