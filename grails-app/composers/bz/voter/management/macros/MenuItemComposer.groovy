package bz.voter.management.macros

import org.zkoss.zk.grails.composer.*

import org.zkoss.zk.ui.HtmlMacroComponent
import org.zkoss.zk.ui.ext.AfterCompose
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Label
import org.zkoss.zul.Div

class MenuItemComposer extends HtmlMacroComponent implements AfterCompose {

   def label
   @Wire
   def menuItemDiv
   @Wire
   def menuLabel

   public MenuItemComposer(){
      super.afterCompose()
   }

   def onCreate(){
      menuLabel.value = label ?: ' '
   }

}
