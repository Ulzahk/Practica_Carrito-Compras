
package com.prcarritocompras.modelodao;

import com.prcarritocompras.config.clsConexion;
import com.prcarritocompras.modelo.Carrito;
import com.prcarritocompras.modelo.Compras;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class CompraDAO {
    
    Connection conn;
    clsConexion cn = new clsConexion();
    PreparedStatement ps;
    ResultSet rs;
    int r = 0;
    
    public int GenerarCompra(Compras compras){
        int compId;
        String sql = "insert into pr_compras (clieId, pagoId, compFecha, compMonto, compEstado) values (?,?,?,?,?)";
        try {
            conn=cn.getConexion();
            ps=conn.prepareStatement(sql);
            ps.setInt(1, compras.getCliente().getId());
            ps.setInt(2, compras.getIdpago());
            ps.setString(3, compras.getFecha());
            ps.setDouble(4, compras.getMonto());
            ps.setString(5, compras.getEstado());
            r = ps.executeUpdate();
            
            sql = "Select @@IDENTITY AS compId";
            
            rs = ps.executeQuery(sql);
            rs.next();
            compId=rs.getInt("compId");
            
            for (Carrito detalle: compras.getDetalleCompras()){
                sql="insert into pr_detalle_compras(prodId, compId, detaCantidad, detaPrecioCompra) values (?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, detalle.getProdId());
                ps.setInt(2, compId);
                ps.setInt(3, detalle.getCantidad());
                ps.setDouble(4, detalle.getPrecioCompra());
                r = ps.executeUpdate();
            }
        } catch (Exception e) {
        }
        return r;
    }
}
