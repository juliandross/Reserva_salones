import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  public titulo: string = 'Sistema de reserva de salones';
  public facultad: string = 'Facultad de Electronica y telecomunicaciones';
  public nombreUsuario: string = 'George Brown';
  public rol: string = 'Administrador';
  
  constructor(private router: Router) {}

  // Método para verificar si la ruta actual coincide con una específica
  isRoute(route: string): boolean {
    return this.router.url === route;
  }
}
