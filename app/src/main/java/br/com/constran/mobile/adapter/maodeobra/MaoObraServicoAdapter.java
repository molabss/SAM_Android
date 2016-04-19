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
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class MaoObraServicoAdapter extends AbstractAdapter<EventoEquipeVO> {

    private final int LIMIT = 24; //tamanho limite do texto da equipe

    public MaoObraServicoAdapter(Context context, List<EventoEquipeVO> list) {
        super(context, R.layout.mao_obra_servico_item, list);
    }

    @Override
    protected void preencherItensGrid(View view, int i) {
        TextView dataTextView = (TextView) view.findViewById(R.id.dataTextView);
        TextView equipeTextView = (TextView) view.findViewById(R.id.equipeTextView);
        TextView localizacaoTextView = (TextView) view.findViewById(R.id.localizacaoTextView);
        ImageView produtividadeImage = (ImageView) view.findViewById(R.id.produtividadeImage);

        EventoEquipeVO eventoEquipeVO = list.get(i);

        dataTextView.setText(Util.toSimpleHourFormat(eventoEquipeVO.getData()));
        equipeTextView.setText(Util.getInitTxt(Util.toCamelCase(eventoEquipeVO.getEquipe().getApelido()), LIMIT));
        LocalizacaoVO localizacao = eventoEquipeVO.getLocalizacao();
        localizacaoTextView.setText(localizacao != null ? localizacao.getDescricao() : "");

        dataTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EDICAO;
                return false;
            }
        });

        equipeTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EDICAO;
                return false;
            }
        });

        localizacaoTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EDICAO;
                return false;
            }
        });

        produtividadeImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.SELECAO;
                return false;
            }
        });
    }
}

