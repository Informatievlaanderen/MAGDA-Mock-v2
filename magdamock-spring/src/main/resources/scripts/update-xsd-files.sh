#!/bin/bash

# NOTE: assuming it is executed with PWD/magda.xsd as the directory with the xsd files

work_dir=$(dirname $0)

$work_dir/patches/replaces.sh
git apply $work_dir/patches/KBI-01.00-Simple.patch  $work_dir/patches/KBI-02.00-Simple.patch  $work_dir/patches/KSZ-INSS.patch  $work_dir/patches/KSZ-SSIN.patch  $work_dir/patches/LightXmlSchema-A011v001.patch --whitespace=fix --ignore-whitespace --allow-empty
