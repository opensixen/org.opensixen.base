/******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */

package org.opensixen.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MTax;
import org.compiere.util.Env;
import org.opensixen.interfaces.DocLine;
import org.opensixen.interfaces.DocWithAmounts;


/**
 * 
 * Tax calculator 
 * 
 * Calculate taxes for documents
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class TaxCalculator {

	
	/**
	 * Calculate tax forom lines
	 * @param ctx
	 * @param lines
	 * @param taxIncluded
	 * @param scale
	 * @return
	 */
	public static List<TaxLine> calc(Properties ctx, DocWithAmounts doc, DocLine[] lines)	{
		// Cache for taxes
		HashMap<Integer, MTax[]> cache_tax = new HashMap<Integer,MTax[]>();
		
		// ArrayList with the taxes of this document
		ArrayList<TaxLine> taxes = new ArrayList<TaxLine>();
		
		// for each line, get their tax and calculeta
		for (int i=0; i < lines.length; i++)	{
			int C_Tax_ID = lines[i].getC_Tax_ID();
			MTax[] line_taxes = null;
			
			// Get taxes from cache 
			if (cache_tax.containsKey(C_Tax_ID))	{
				line_taxes = cache_tax.get(C_Tax_ID);
			}
			// Or load and caching
			else {
				MTax base_tax = new MTax(ctx, C_Tax_ID, null);
				if (base_tax.isSummary())	{
					line_taxes = base_tax.getChildTaxes(false);
				}
				else {
					line_taxes = new MTax[]{base_tax};
				}
				cache_tax.put(C_Tax_ID, line_taxes);
			}
			
			// For each tax in this line
			for (int x=0; x < line_taxes.length; x++)	{
				// calculate tax
				MTax tax = line_taxes[x];
				BigDecimal lineNet = lines[i].getLineNetAmt();
				BigDecimal taxAmt = Env.ZERO;
				if (tax.isDocumentLevel() == false) {
					taxAmt = tax.calculateTax(lineNet, doc.isTaxIncluded(), doc.getPrecision());
				}
				// Only aply taxes in Sales or purchause type match
				if ((tax.getSOPOType().equals(tax.SOPOTYPE_PurchaseTax) && doc.isSOTrx()) 
						|| (tax.getSOPOType().equals(tax.SOPOTYPE_SalesTax) && doc.isSOTrx() == false))	{
					continue;
				}
				boolean find = false;
				// If there are lines with the same tax
				// plus amounts
				for (TaxLine l:taxes)	{
					if (l.getTax().getC_Tax_ID() == tax.getC_Tax_ID())	{
						l.addTaxAmt(taxAmt);
						l.addTaxBaseAmt(lineNet);
						find = true;
						continue;
					}
				}

				// If no find, create new lines
				if (!find) {
					TaxLine l = new TaxLine(tax, taxAmt, lineNet);
					taxes.add(l);
				}
			}
		}
		
		// After calc each tax in each line
		// fixup document Level taxes
		// recalculating at doc level
		for (TaxLine line:taxes)	{
			// if this tax is doc level
			// recalculate from tax base total
			if (line.getTax().isDocumentLevel())	{
				BigDecimal amt = line.getTax().calculateTax(line.getTaxBaseAmt(), doc.isTaxIncluded(), doc.getPrecision());
				line.setTaxAmt(amt);
			}
		}
				
		return taxes;
	}
	
}