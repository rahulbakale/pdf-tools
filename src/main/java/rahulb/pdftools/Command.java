package rahulb.pdftools;

enum Command {
  EncryptPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new EncryptPdf();
    }
  },

  DecryptPdfs {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new DecryptPdfs();
    }
  },

  PdfToImage {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new PdfToImage();
    }
  },

  RemovePages {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new RemovePages();
    }
  },

  AddWatermark {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new AddWatermark();
    }
  },

  ConvertToGrayscale {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new ConvertToGrayscale();
    }
  },

  ImagesToPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new ImagesToPdf();
    }
  },

  Pipeline {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new Pipeline();
    }
  };

  abstract AbstractCommandHandler obtainCommandHandler();
}
