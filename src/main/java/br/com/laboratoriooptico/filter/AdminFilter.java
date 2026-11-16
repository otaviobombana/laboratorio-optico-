package br.com.laboratoriooptico.filter;

import br.com.laboratoriooptico.controller.LoginController;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro administrativo: toda página dentro de /admin/ exige usuário logado.
 * Quem não estiver logado é mandado para a tela de login.
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession sessao = request.getSession(false);
        boolean logado = sessao != null
                && sessao.getAttribute(LoginController.USUARIO_LOGADO) != null;

        if (logado) {
            // Evita que o navegador mostre páginas do cache depois do logout
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            chain.doFilter(req, resp);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
        }
    }
}
