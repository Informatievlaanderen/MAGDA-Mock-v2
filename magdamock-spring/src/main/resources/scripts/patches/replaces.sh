#!/bin/sh

xsd_dir=magda.xsd

sed -i 's/webservice.registreernotificatiestatusuittrekseldienst-02_00.justitie-02_00.vip.vlaanderen.be/service.registreernotificatiestatusuittrekseldienst-02_00.justitie-02_00.vip.vlaanderen.be/g' $xsd_dir/Justitie.RegistreerNotificatieStatusUittrekselDienst-02.00/Service/*

perl -pi -e 's/\Q[0-9][0-9](([4][0-9])|([5][0-2]))(([0-2][0-9])|([3][0-1]))[0-9]{3}(([0-8][0-9])|([9][0-7]))\E/[0-9][0-9](([4][0-9])|([5][0-2])|([6][0-9])|([7][0-2]))(([0-2][0-9])|([3][0-1]))[0-9]{3}(([0-8][0-9])|([9][0-7]))/' $xsd_dir/Bronnen/KSZ/be/fgov/kszbcss/LightXmlSchema/A011v001.xsd
