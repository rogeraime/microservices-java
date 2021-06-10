export class Laundry {
  id: number;
  submissionDate: any;
  acceptedDate: any;
  deliveryDate: any;
  paid: number;
  fetchedByCleaners: boolean;
  fetchedByOwner: boolean;
  laundryOwner: string;
  totalCost: number;
  clothes: any;
  image: string;
  eMail: string;
  completed: boolean;

  constructor(
    Id ? : number,
    SubmissionDate ? : any,
    AcceptedDate ? : any,
    DeliveryDate ? : any,
    Paid ? : number,
    FetchedByCleaners ? : boolean,
    FetchedByOwner ? : boolean,
    LaundryOwner ? : string,
    TotalCost ? : number,
    Clothes ? : any, 
    image ? : string,
    eMail ? : string,
    completed ? : boolean) {

    this.id = Id;
    this.submissionDate = SubmissionDate;
    this.acceptedDate = AcceptedDate;
    this.deliveryDate = DeliveryDate;
    this.paid = Paid;
    this.fetchedByCleaners = FetchedByCleaners;
    this.fetchedByOwner = FetchedByOwner;
    this.laundryOwner = LaundryOwner;
    this.totalCost = TotalCost;
    this.clothes = Clothes;
    this.image = image;
    this.eMail = eMail;
    this.completed = completed;
  }


}
