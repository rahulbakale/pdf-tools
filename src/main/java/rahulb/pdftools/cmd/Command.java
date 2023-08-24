package rahulb.pdftools.cmd;

import rahulb.pdftools.core.AddWatermarkService;
import rahulb.pdftools.core.ConvertToGrayscaleService;
import rahulb.pdftools.core.DecryptPdfsService;
import rahulb.pdftools.core.EncryptPdfService;

enum Command {
  EncryptPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new EncryptPdfHandler(new EncryptPdfService());
    }
  },

  DecryptPdfs {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new DecryptPdfsHandler(new DecryptPdfsService());
    }
  },

  PdfToImage {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new PdfToImageHandler();
    }
  },

  RemovePages {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new RemovePagesHandler();
    }
  },

  AddWatermark {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new AddWatermarkHandler(new AddWatermarkService());
    }
  },

  ConvertToGrayscale {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new ConvertToGrayscaleHandler(new ConvertToGrayscaleService());
    }
  },

  ImagesToPdf {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new ImagesToPdfHandler();
    }
  },

  Pipeline {
    @Override
    AbstractCommandHandler obtainCommandHandler() {
      return new PipelineHandler();
    }
  };

  abstract AbstractCommandHandler obtainCommandHandler();
}
