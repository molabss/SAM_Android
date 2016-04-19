package br.com.constran.mobile.adapter.maodeobra;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.enums.Operacao;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.IntegranteVO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.LocalizacaoEquipeVO;

import java.util.List;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class LocalEquipeAdapter extends AbstractAdapter<LocalizacaoEquipeVO> {

    public LocalEquipeAdapter(Context context, List<LocalizacaoEquipeVO> locaisEquipes) {
        super(context, R.layout.equipe_grid_item, locaisEquipes);
    }

    @Override
    protected void preencherItensGrid(View view, int i) {
        TextView equipeText = (TextView) view.findViewById(R.id.equipeText);
        ImageView equipeImage = (ImageView) view.findViewById(R.id.equipeImage);
        ImageView excluirImage = (ImageView) view.findViewById(R.id.excluirImage);

        final LocalizacaoEquipeVO localizacaoEquipe = list.get(i);

        equipeText.setText(localizacaoEquipe.getEquipe().getApelido());

        List<IntegranteVO> integrantes = localizacaoEquipe.getEquipe().getIntegrantes();

        if (integrantes != null && !integrantes.isEmpty()) {
            equipeImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    operacao = Operacao.EDICAO;
                    return false;
                }
            });
        } else {
            equipeImage.setEnabled(false);
            equipeImage.setImageResource(R.drawable.equipe3);
        }

        excluirImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EXCLUSAO;
                return false;
            }
        });
    }
}

