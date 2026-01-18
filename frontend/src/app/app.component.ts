import { Component } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, CommonModule],
  template: `
    <nav class="navbar glass-card">
      <div class="nav-brand">
        <i class="fas fa-shield-halved"></i>
        <span>BIP Benefícios</span>
      </div>
      <div class="nav-links">
        <a routerLink="/beneficios" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
          <i class="fas fa-list"></i> Lista
        </a>
        <a routerLink="/beneficios/novo" routerLinkActive="active">
          <i class="fas fa-plus-circle"></i> Novo
        </a>
        <span style="font-size: 0.6rem; opacity: 0.3; margin-left: 1rem;">v2.0</span>
      </div>
    </nav>

    <main class="main-content">
      <router-outlet></router-outlet>
    </main>

    <style>
      .navbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 1rem 2rem;
        margin-bottom: 2rem;
        border-radius: 12px;
      }
      .nav-brand {
        display: flex;
        align-items: center;
        gap: 0.75rem;
        font-size: 1.5rem;
        font-weight: 700;
        color: white;
      }
      .nav-brand i {
        color: var(--primary);
      }
      .nav-links {
        display: flex;
        gap: 1.5rem;
      }
      .nav-links a {
        color: var(--text-secondary);
        text-decoration: none;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 0.5rem;
        transition: all 0.3s ease;
      }
      .nav-links a:hover, .nav-links a.active {
        color: white;
      }
      .nav-links a.active {
        color: var(--primary);
      }
      .main-content {
        animation: fadeIn 0.5s ease-out;
      }
      @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
      }
    </style>
`
})
export class AppComponent { }