package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen


class ImportVotersModalComposer extends GrailsComposer {

	def importLabel

    def afterCompose = { window ->
	 	importLabel.setValue("Import voters!")
    }
}
