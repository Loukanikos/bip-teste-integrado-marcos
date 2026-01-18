import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BeneficiosService } from './beneficios.service';
import { BeneficioDTO } from './beneficio.dto';
import { TransferRequest } from './transfer-request';

@Component({
    selector: 'app-beneficios-transfer',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    template: `
    <div class="container">
      <h2>Transferir Benefício</h2>
      
      <div class="glass-card">
        <form (ngSubmit)="transferir()">
          <div class="form-group">
            <label>Origem</label>
            <input [value]="origem.nome + ' (Saldo: R$ ' + origem.valor + ')'" disabled />
          </div>

          <div class="form-group">
            <label>Destino</label>
            <select [(ngModel)]="request.toId" name="toId" required>
              <option [ngValue]="undefined" disabled>Selecione o destino</option>
              <option *ngFor="let b of destinos" [ngValue]="b.id">
                {{ b.nome }} (#{{ b.id }})
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>Valor da Transferência</label>
            <input type="number" [(ngModel)]="request.amount" name="amount" [max]="origem.valor" step="0.01" required />
            <small *ngIf="request.amount > origem.valor" style="color: var(--danger)">
              Saldo insuficiente!
            </small>
          </div>

          <div class="form-actions">
            <button class="btn btn-primary" type="submit" [disabled]="!request.toId || !request.amount || request.amount > origem.valor">
              <i class="fas fa-exchange-alt"></i> Transferir
            </button>
            <button class="btn btn-outline" type="button" (click)="voltar()">
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>

    <style>
      .container { max-width: 600px; margin: 0 auto; }
      .form-group { margin-bottom: 1.5rem; }
      .form-actions { display: flex; gap: 1rem; margin-top: 2rem; }
    </style>
  `
})
export class BeneficiosTransferComponent implements OnInit {
    origem: BeneficioDTO = { nome: '', valor: 0 };
    destinos: BeneficioDTO[] = [];
    request: TransferRequest = { fromId: 0, toId: 0, amount: 0 };

    constructor(
        private beneficiosService: BeneficiosService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.request.fromId = +id;
            this.beneficiosService.getById(+id).subscribe(data => this.origem = data);
            this.beneficiosService.list().subscribe(list => {
                this.destinos = list.filter(b => b.id !== +id);
            });
        }
    }

    transferir() {
        this.beneficiosService.transfer(this.request).subscribe(() => {
            alert('Transferência realizada com sucesso!');
            this.voltar();
        });
    }

    voltar() {
        this.router.navigate(['/beneficios']);
    }
}
