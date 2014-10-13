function sPath = mls_supporter_save_picture(hService, sRoot)

sPath = char(hService.savePicture(sRoot));
return;
end