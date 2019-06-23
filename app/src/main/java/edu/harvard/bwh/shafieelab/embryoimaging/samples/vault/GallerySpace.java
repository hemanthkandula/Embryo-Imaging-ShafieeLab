package edu.harvard.bwh.shafieelab.embryoimaging.samples.vault;

import com.contentful.vault.Space;

import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Const;

@Space(value = Const.SPACE_ID, models = {Author.class, Gallery.class, Image.class}, locales = {"en-US"})
public class GallerySpace {
}
